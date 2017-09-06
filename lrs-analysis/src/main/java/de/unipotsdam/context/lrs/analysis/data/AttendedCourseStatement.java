package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class AttendedCourseStatement {

	private CourseName _id;
	private List<Semester> semester;

	public List<Semester> getSemester() {
		return semester;
	}

	public void setSemester(List<Semester> semester) {
		this.semester = semester;
	}

	public CourseName get_id() {
		return _id;
	}

	public void set_id(CourseName _id) {
		this._id = _id;
	}
}
