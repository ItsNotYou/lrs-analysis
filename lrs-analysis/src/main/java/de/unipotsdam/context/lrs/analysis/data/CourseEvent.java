package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class CourseEvent {

	private String id;
	private List<String> parents;
	private List<String> groupings;
	private List<String> others;

	public CourseEvent() {
	}

	public CourseEvent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
