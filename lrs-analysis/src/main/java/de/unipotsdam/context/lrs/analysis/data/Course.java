package de.unipotsdam.context.lrs.analysis.data;

public class Course {

	private String name;
	private String semesterStart;
	private String semesterEnd;
	private String semesterId;

	public String getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(String semesterId) {
		this.semesterId = semesterId;
	}

	public String getSemesterStart() {
		return semesterStart;
	}

	public void setSemesterStart(String semesterStart) {
		this.semesterStart = semesterStart;
	}

	public String getSemesterEnd() {
		return semesterEnd;
	}

	public void setSemesterEnd(String semesterEnd) {
		this.semesterEnd = semesterEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
