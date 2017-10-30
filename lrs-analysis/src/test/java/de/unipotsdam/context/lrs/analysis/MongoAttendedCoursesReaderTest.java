package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.MongoAttendedCoursesReader;
import de.unipotsdam.context.lrs.analysis.data.CourseEvent;

public class MongoAttendedCoursesReaderTest {

	private MongoAttendedCoursesReader sut;

	@Before
	public void before() {
		sut = new MongoAttendedCoursesReader();
	}

	@Test
	public void shouldReturnList() throws Exception {
		List<CourseEvent> result = sut.getCurrentlyAttendedCourses("hgessner");
		assertNotNull(result);
	}
}
