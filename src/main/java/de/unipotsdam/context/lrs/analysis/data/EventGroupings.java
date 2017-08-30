package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class EventGroupings {

	private String _id;
	private List<EventStatement> statements;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public List<EventStatement> getStatements() {
		return statements;
	}

	public void setStatements(List<EventStatement> statements) {
		this.statements = statements;
	}
}
