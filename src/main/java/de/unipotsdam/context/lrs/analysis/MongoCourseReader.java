package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import de.unipotsdam.context.lrs.analysis.data.AttendedCourseStatement;
import de.unipotsdam.context.lrs.analysis.data.AttendedCoursesResponse;
import de.unipotsdam.context.lrs.analysis.data.Course;
import de.unipotsdam.context.lrs.analysis.data.CourseName;

public class MongoCourseReader implements Callable<Collection<Course>> {

	private String ldapShortname;

	public MongoCourseReader(String ldapShortname) {
		this.ldapShortname = ldapShortname;
	}

	@Override
	public Collection<Course> call() throws Exception {
		return readAttendedCoursesOf();
	}

	public Collection<Course> readAttendedCoursesOf() {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.context.contextActivities.category.id", "http://xapi.trainingevidencesystems.com/recipes/attendance/0_0_1#simple", "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.object.definition.name")));

		AttendedCoursesResponse response = new LRS().query(pipeline, AttendedCoursesResponse.class);
		return asCourses(response.getResult());
	}

	private Collection<Course> asCourses(Collection<AttendedCourseStatement> attendedCourses) {
		Collection<Course> result = new ArrayList<>();

		for (AttendedCourseStatement acs : attendedCourses) {
			// Select language
			CourseName name = acs.get_id();
			if (name.getDeDE() != null) {
				result.add(new Course(name.getDeDE()));
			} else if (name.getEnUS() != null) {
				result.add(new Course(name.getEnUS()));
			}
		}

		return result;
	}
}
