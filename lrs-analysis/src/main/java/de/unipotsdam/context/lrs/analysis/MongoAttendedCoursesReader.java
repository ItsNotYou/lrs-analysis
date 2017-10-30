package de.unipotsdam.context.lrs.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.unipotsdam.context.lrs.analysis.data.CourseEvent;
import de.unipotsdam.context.lrs.analysis.data.CourseEventsResponse;
import de.unipotsdam.context.lrs.analysis.data.EventGroupings;
import de.unipotsdam.context.lrs.analysis.data.EventStatement;

public class MongoAttendedCoursesReader {

	private Map<String, Object> map(Object... keyValuePairs) {
		Map<String, Object> result = new HashMap<>();
		for (int count = 0; count < keyValuePairs.length; count += 2) {
			result.put((String) keyValuePairs[count], keyValuePairs[count + 1]);
		}
		return result;
	}

	public List<CourseEvent> getCurrentlyAttendedCourses(String ldapShortname) throws IOException {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		// TODO: Tweak query to only include opened, closed, adjorned, resumed, user joined and user left
		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.context.contextActivities.category.id", "http://xapi.trainingevidencesystems.com/recipes/attendance/0_0_1#detailed", "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.object.id", "statements", map("$push", "$statement"))));
		pipeline.add(map("$project", map("statements", map("actor", 1, "verb", 1, "timestamp", 1, "object", map("definition", map("name", 1))))));

		CourseEventsResponse events = new LRS().query(pipeline, CourseEventsResponse.class);
		List<EventGroupings> groupings = events.getResult();

		// check for attendance
		for (Iterator<EventGroupings> it = groupings.iterator(); it.hasNext();) {
			EventGroupings grouping = it.next();
			if (!isAttendedBy(user, grouping)) {
				it.remove();
			}
		}

		// print intermediate results
		for (EventGroupings grouping : groupings) {
			System.out.println("attending " + grouping.get_id());
		}

		// extract course event
		List<CourseEvent> result = new ArrayList<>(groupings.size());
		for (EventGroupings grouping : groupings) {
			result.add(new CourseEvent(grouping.get_id()));
		}

		return result;
	}

	// For each group
	// - Sort by timestamp
	// - Run events in order and track state
	// - Add event to results if
	// --- event is running and
	// --- user joined
	// --- Open statements can be missing
	private boolean isAttendedBy(String user, EventGroupings grouping) {
		// Sort by timestamp
		List<EventStatement> statements = grouping.getStatements();
		Collections.sort(statements, new Comparator<EventStatement>() {
			public int compare(EventStatement o1, EventStatement o2) {
				return o1.asTimestamp().compareTo(o2.asTimestamp());
			}
		});

		// Replay event
		Boolean isRunning = null;
		boolean isAttending = false;
		for (EventStatement stmt : grouping.getStatements()) {
			String verbId = stmt.getVerb().getId();
			switch (verbId) {
			case "http://activitystrea.ms/schema/1.0/open":
				isRunning = true;
				break;
			case "http://activitystrea.ms/schema/1.0/close":
				isRunning = false;
				break;
			case "http://id.tincanapi.com/verb/adjourned":
				isRunning = false;
				break;
			case "http://adlnet.gov/expapi/verbs/resumed":
				isRunning = true;
				break;
			case "http://activitystrea.ms/schema/1.0/join":
				if (stmt.getActor().getMbox().equals(user)) {
					isAttending = true;
				}
				break;
			case "http://activitystrea.ms/schema/1.0/leave":
				if (stmt.getActor().getMbox().equals(user)) {
					isAttending = false;
				}
				break;
			}
		}

		// We have to do a Boolean false check because isRunning uses a three valued truth with yes (true), no (false) and unknown (null)
		// and we want to recognise both true and null as true
		boolean isAssumedRunning = isRunning == null || isRunning;

		// Check if event is assumed running and user is attending
		return isAssumedRunning && isAttending;
	}
}
