package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class CurrentlyAttendedCoursesResponse {

	private List<CourseEvent> result;

	public CurrentlyAttendedCoursesResponse(List<CourseEvent> result) {
		this.result = result;
	}

	public List<CourseEvent> getResult() {
		return result;
	}
}
