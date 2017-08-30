package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class CourseEventsResponse {

	private List<EventGroupings> result;
	private Integer ok;

	public List<EventGroupings> getResult() {
		return result;
	}

	public void setResult(List<EventGroupings> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
