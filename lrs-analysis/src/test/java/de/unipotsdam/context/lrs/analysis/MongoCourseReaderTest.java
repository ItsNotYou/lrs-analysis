package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.Course;

public class MongoCourseReaderTest {

	@Test
	public void shouldReadAttendedCourses() throws Exception {
		Collection<Course> result = new MongoCourseReader("hgessner").call();
		for (Course course : result) {
			System.out.println(course.getName());
		}
		assertFalse(result.isEmpty());
	}
}
