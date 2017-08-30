package de.unipotsdam.context.lrs.analysis.filter;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.CourseEvent;

public class CoursesFilterTest {

	private CoursesFilter sut;

	@Before
	public void before() {
		sut = new CoursesFilter();
	}

	@Test
	public void shouldReturnList() {
		List<CourseEvent> result = sut.getCurrentlyAttendedCourses("hgessner");
		assertNotNull(result);
	}
}
