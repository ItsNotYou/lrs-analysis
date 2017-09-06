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
import de.unipotsdam.context.lrs.analysis.data.Semester;

public class MongoCourseReader implements Callable<Collection<Course>> {

	private String ldapShortname;

	public MongoCourseReader(String ldapShortname) {
		this.ldapShortname = ldapShortname;
	}

	/**
	 * Reads attended courses of {@link #ldapShortname}
	 */
	@Override
	public Collection<Course> call() throws Exception {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.context.contextActivities.category.id", "http://xapi.trainingevidencesystems.com/recipes/attendance/0_0_1#simple", "statement.context.contextActivities.other.definition.type", "http://id.tincanapi.com/activitytype/semester", "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.object.definition.name", "semester", map("$addToSet", "$statement.context.contextActivities.other.definition.extensions.http://id&46;tincanapi&46;com/extension/semester"))));

		AttendedCoursesResponse response = new LRS().query(pipeline, AttendedCoursesResponse.class);
		return asCourses(response.getResult());
	}

	private Collection<Course> asCourses(Collection<AttendedCourseStatement> attendedCourses) {
		Collection<Course> result = new ArrayList<>();

		for (AttendedCourseStatement acs : attendedCourses) {
			for (Semester semester : acs.getSemester()) {
				Course course = new Course();

				// Set semester
				course.setSemesterId(semester.getId());
				course.setSemesterStart(semester.getStart());
				course.setSemesterEnd(semester.getEnd());

				// Select language
				CourseName name = acs.get_id();
				if (name.getDeDE() != null) {
					course.setName(name.getDeDE());
					result.add(course);
				} else if (name.getEnUS() != null) {
					course.setName(name.getEnUS());
					result.add(course);
				}
			}
		}

		return result;
	}
}
