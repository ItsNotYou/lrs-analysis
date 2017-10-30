package de.unipotsdam.context.lrs.analysis.filter;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.unipotsdam.context.lrs.analysis.MongoAttendedCoursesReader;
import de.unipotsdam.context.lrs.analysis.MongoWorkspaceReader;
import de.unipotsdam.context.lrs.analysis.data.CourseEvent;
import de.unipotsdam.context.lrs.analysis.data.CourseWorkspacePair;
import de.unipotsdam.context.lrs.analysis.data.CurrentlyAttendedCoursesResponse;

public class RunningCoursesFilter implements Closeable {

	private ExecutorService threadPool;

	public RunningCoursesFilter() {
		this.threadPool = Executors.newFixedThreadPool(8);
	}

	public CurrentlyAttendedCoursesResponse getCurrentlyAttendedCourses(String ldapShortname) throws InterruptedException, ExecutionException, IOException {
		List<CourseEvent> attendedCourses = new MongoAttendedCoursesReader().getCurrentlyAttendedCourses(ldapShortname);

		Collection<Future<List<CourseWorkspacePair>>> workspaces = new ArrayList<>(attendedCourses.size());
		for (CourseEvent course : attendedCourses) {
			workspaces.add(threadPool.submit(new MongoWorkspaceReader(course, ldapShortname)));
		}

		Collection<CourseWorkspacePair> result = new ArrayList<>(workspaces.size());
		for (Future<List<CourseWorkspacePair>> workspace : workspaces) {
			result.addAll(workspace.get());
		}

		for (CourseWorkspacePair pair : result) {
			System.out.println("Course ID " + pair.getCourseId());
			System.out.println("Workspace ID " + pair.getWorkspaceId());
			System.out.println("Group ID " + pair.getGroupId());
		}

		CurrentlyAttendedCoursesResponse response = new CurrentlyAttendedCoursesResponse();
		response.setCourses(result);
		return response;
	}

	@Override
	public void close() throws IOException {
		threadPool.shutdown();
	}
}
