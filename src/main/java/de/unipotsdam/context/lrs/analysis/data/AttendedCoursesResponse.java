package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class AttendedCoursesResponse {

	private List<AttendedCourseStatement> result;
	private Integer ok;

	public List<AttendedCourseStatement> getResult() {
		return result;
	}

	public void setResult(List<AttendedCourseStatement> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
