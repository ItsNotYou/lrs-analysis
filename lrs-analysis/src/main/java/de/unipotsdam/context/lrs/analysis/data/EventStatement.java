package de.unipotsdam.context.lrs.analysis.data;

import java.util.Calendar;

public class EventStatement {

	private Actor actor;
	private Verb verb;
	private EventObject object;
	private String timestamp;

	public Calendar asTimestamp() {
		return javax.xml.bind.DatatypeConverter.parseDateTime(timestamp);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public EventObject getObject() {
		return object;
	}

	public void setObject(EventObject object) {
		this.object = object;
	}
}
