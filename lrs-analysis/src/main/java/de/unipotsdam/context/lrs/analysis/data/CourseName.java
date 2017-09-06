package de.unipotsdam.context.lrs.analysis.data;

import javax.xml.bind.annotation.XmlAttribute;

public class CourseName {

	private String enUS;
	private String deDE;
	private String semesterStart;

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

	private String semesterEnd;

	@XmlAttribute(name = "en-US")
	public String getEnUS() {
		return enUS;
	}

	public void setEnUS(String enUS) {
		this.enUS = enUS;
	}

	@XmlAttribute(name = "de-DE")
	public String getDeDE() {
		return deDE;
	}

	public void setDeDE(String deDE) {
		this.deDE = deDE;
	}
}
