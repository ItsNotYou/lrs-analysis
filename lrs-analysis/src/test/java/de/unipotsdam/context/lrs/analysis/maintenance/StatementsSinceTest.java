package de.unipotsdam.context.lrs.analysis.maintenance;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.LRS;
import de.unipotsdam.context.lrs.analysis.data.CourseWorkspacePair;
import de.unipotsdam.context.lrs.analysis.data.CurrentlyAttendedCoursesResponse;
import de.unipotsdam.context.lrs.analysis.filter.RunningCoursesFilter;

/**
 * Use these tests for simple management purposes. They are ignored while building and must be manually activated
 * 
 * @author hgessner
 */
public class StatementsSinceTest {

	/**
	 * Shows the IDs of all valid (non-voided) statements created after "since"
	 * 
	 * @throws IOException
	 */
	@Ignore
	@Test
	public void shouldGetValidStatementsSince() throws IOException {
		String since = "2017-10-26T12:00:00+00:00";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.stored", map("$gt", since), "statement.verb.id", map("$ne", "http://adlnet.gov/expapi/verbs/voided"), "voided", false)));
		pipeline.add(map("$project", map("_id", 0, "id", "$statement.id")));

		StatementSinceResponse result = new LRS().query(pipeline, StatementSinceResponse.class);
		for (StatementId stmt : result.getResult()) {
			System.out.println("Since-ID: " + stmt.getId());
		}
	}

	/**
	 * Shows the course IDs of all courses currently attended by "user"
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void getCurrentlyAttendedCourses() throws Exception {
		String user = "marmuster";

		try (RunningCoursesFilter rcf = new RunningCoursesFilter()) {
			CurrentlyAttendedCoursesResponse result = rcf.getCurrentlyAttendedCourses(user);
			for (CourseWorkspacePair r : result.getCourses()) {
				System.out.println("Running: " + r.getCourseId());
			}
		}
	}
}
