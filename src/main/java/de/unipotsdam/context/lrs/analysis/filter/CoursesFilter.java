package de.unipotsdam.context.lrs.analysis.filter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.uri.UriComponent;

import com.google.gson.Gson;

import de.unipotsdam.context.lrs.analysis.data.CourseEvent;
import de.unipotsdam.context.lrs.analysis.data.CourseEventsResponse;
import de.unipotsdam.context.lrs.analysis.data.EventGroupings;
import de.unipotsdam.context.lrs.analysis.data.EventStatement;
import gov.adlnet.xapi.util.Base64;

public class CoursesFilter {

	private static final String lrsUrl = "http://lrs.soft.cs.uni-potsdam.de/";
	private static final String username = "f1e520976fb3cd27127bef0bfd2c4af924bfd2fc";
	private static final String password = "b4f0955aea62c4d9f94a98e32a400e665f7338a7";

	private <T> T readFromLrs(Object pipeline, Class<T> responseEntity) {
		String pipeJson = asJson(pipeline);

		String loginData = username + ":" + password;
		String login = ("Basic " + Base64.encodeToString(loginData.getBytes(), Base64.DEFAULT)).replace("\n", "");

		Client client = ClientBuilder.newClient();
		Response response = client.target(lrsUrl).path("/api/v1/statements/aggregate").queryParam("pipeline", pipeJson).request().header("Authorization", login).get();
		return response.readEntity(responseEntity);
	}

	private Map<String, Object> map(Object... keyValuePairs) {
		Map<String, Object> result = new HashMap<>();
		for (int count = 0; count < keyValuePairs.length; count += 2) {
			result.put((String) keyValuePairs[count], keyValuePairs[count + 1]);
		}
		return result;
	}

	private String asJson(Object object) {
		String json = new Gson().toJson(object);
		return UriComponent.contextualEncode(json, UriComponent.Type.QUERY_PARAM, false);
	}

	public List<CourseEvent> getCurrentlyAttendedCourses(String ldapShortname) {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		// TODO: Tweak query to only include opened, closed, adjorned, resumed, user joined and user left
		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.context.contextActivities.category.id", "http://xapi.trainingevidencesystems.com/recipes/attendance/0_0_1#detailed", "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.object.id", "statements", map("$push", "$statement"))));
		pipeline.add(map("$project", map("statements", map("actor", 1, "verb", 1, "timestamp", 1, "object", map("definition", map("name", 1))))));

		CourseEventsResponse events = readFromLrs(pipeline, CourseEventsResponse.class);
		List<EventGroupings> groupings = events.getResult();

		// check for attendance
		Predicate<EventGroupings> isNotAttended = new Not<EventGroupings>(new IsAttendedBy(user));
		for (Iterator<EventGroupings> it = groupings.iterator(); it.hasNext();) {
			EventGroupings grouping = it.next();
			if (isNotAttended.test(grouping)) {
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

	private class Not<T> implements Predicate<T> {

		private Predicate<T> predicate;

		public Not(Predicate<T> predicate) {
			this.predicate = predicate;
		}

		@Override
		public boolean test(T t) {
			return !predicate.test(t);
		}
	}

	// For each group
	// - Sort by timestamp
	// - Run events in order and track state
	// - Add event to results if
	// --- event is running and
	// --- user joined
	// --- Open statements can be missing
	private class IsAttendedBy implements Predicate<EventGroupings> {

		private String user;

		public IsAttendedBy(String user) {
			this.user = user;
		}

		@Override
		public boolean test(EventGroupings grouping) {
			// Sort by timestamp
			List<EventStatement> statements = grouping.getStatements();
			statements.sort(new Comparator<EventStatement>() {
				public int compare(EventStatement o1, EventStatement o2) {
					return o1.asTimestamp().compareTo(o2.asTimestamp());
				}
			});

			// Replay event
			boolean isRunning = false;
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

			// Check if event is running and user is attending
			return isRunning && isAttending;
		}
	}
}
