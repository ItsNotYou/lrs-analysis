package de.unipotsdam.context.lrs.analysis.filter;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.CurrentlyAttendedCoursesResponse;

public class RunningCoursesFilterTest {

	@Test
	public void shouldReturnWorkspace() throws Exception {
		try (RunningCoursesFilter sut = new RunningCoursesFilter()) {
			CurrentlyAttendedCoursesResponse result = sut.getCurrentlyAttendedCourses("hgessner");
			assertFalse(result.getCourses().isEmpty());
		}
	}
}
