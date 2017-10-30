package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unipotsdam.context.lrs.analysis.data.CourseEvent;
import de.unipotsdam.context.lrs.analysis.data.CourseWorkspacePair;
import de.unipotsdam.context.lrs.analysis.data.WorkspaceResponse;
import de.unipotsdam.context.lrs.analysis.data.WorkspaceStatement;

public class MongoWorkspaceReader implements Callable<List<CourseWorkspacePair>> {

	private static final Logger log = Logger.getLogger(MongoWorkspaceReader.class.getName());

	private CourseEvent course;
	private String ldapShortname;

	public MongoWorkspaceReader(CourseEvent course, String ldapShortname) {
		this.course = course;
		this.ldapShortname = ldapShortname;
	}

	@Override
	public List<CourseWorkspacePair> call() throws Exception {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		// Shorten id to parent course if necessary
		Collection<String> courseIds = getFullCourseHierarchy();

		// Read course attachments
		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.verb.id", "http://activitystrea.ms/schema/1.0/attach", "statement.object.definition.type", "http://xapi.uni-potsdam.de/ple/workspace", "statement.context.contextActivities.parent.id", map("$in", courseIds), "voided", false)));
		pipeline.add(map("$project", map("_id", 0, "workspace", "$statement.object.id")));

		// Extract workspace id
		WorkspaceResponse result = new LRS().query(pipeline, WorkspaceResponse.class);
		return asWorkspace(result.getResult());
	}

	private Collection<String> getFullCourseHierarchy() {
		Collection<String> result = new HashSet<>();
		result.add(course.getId());

		// We expect something like the following
		// http://xapi.uni-potsdam.de/event/20171/60394
		// http://xapi.uni-potsdam.de/event/20171/60394/105411/2017-06-14T22:00:00.000Z
		Matcher matcher = Pattern.compile("^http:\\/\\/xapi\\.uni-potsdam\\.de\\/event\\/\\w*\\/\\w*").matcher(course.getId());
		if (matcher.find()) {
			result.add(matcher.group());
		}

		return result;
	}

	private List<CourseWorkspacePair> asWorkspace(List<WorkspaceStatement> workspaces) {
		ArrayList<CourseWorkspacePair> result = new ArrayList<>(workspaces.size());
		for (WorkspaceStatement workspace : workspaces) {
			// Extract group id
			String groupId = null;
			try {
				String workspacePath = new URI(workspace.getWorkspace()).getPath();
				groupId = workspacePath.substring(workspacePath.lastIndexOf('/') + 1);
			} catch (URISyntaxException ex) {
				log.log(Level.WARNING, "Workspace ID invalid", ex);
			}

			// Set result
			CourseWorkspacePair pair = new CourseWorkspacePair();
			pair.setCourseId(course.getId());
			pair.setWorkspaceId(workspace.getWorkspace());
			pair.setGroupId(groupId);
			result.add(pair);
		}
		return result;
	}
}
