package de.unipotsdam.context.lrs.analysis.data;

import java.util.Collection;

public class CurrentlyAttendedCoursesResponse {

	private Collection<CourseWorkspacePair> courses;

	public Collection<CourseWorkspacePair> getCourses() {
		return courses;
	}

	public void setCourses(Collection<CourseWorkspacePair> result) {
		this.courses = result;
	}
}
