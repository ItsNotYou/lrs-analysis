package de.unipotsdam.context.lrs.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import de.unipotsdam.context.lrs.analysis.data.Place;
import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementReference;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verbs;

public class XapiPlaceReader {

	private static final String lrsUrl = "http://lrs.soft.cs.uni-potsdam.de/data/xAPI/";
	private static final String username = "f1e520976fb3cd27127bef0bfd2c4af924bfd2fc";
	private static final String password = "b4f0955aea62c4d9f94a98e32a400e665f7338a7";

	public List<Place> readPlacesOf(String ldapShortname) throws IOException {
		StatementClient client = new StatementClient(lrsUrl, username, password);

		// Retrieve statements from LRS
		List<Statement> statements = new ArrayList<Statement>();
		Actor actor = new Agent();
		actor.setMbox("mailto:" + ldapShortname + "@uni-potsdam.de");
		StatementResult places = client.limitResults(10000).filterByActor(actor).filterByVerb("http://activitystrea.ms/schema/1.0/at").getStatements();
		statements.addAll(places.getStatements());
		while (places.hasMore()) {
			places = client.getStatements(places.getMore());
			statements.addAll(places.getStatements());
		}

		// Remove voided statements
		List<Statement> voided = new ArrayList<Statement>();
		for (Statement stmt : statements) {
			if (stmt.getVerb().getId().equals(Verbs.voided().getId())) {
				voided.add(stmt);
			}
		}
		statements.removeAll(voided);
		for (Statement stmt : voided) {
			final String target = ((StatementReference) stmt.getObject()).getId();
			for (Iterator<Statement> it = statements.iterator(); it.hasNext();) {
				Statement statement = it.next();
				if (statement.getId().equals(target)) {
					it.remove();
				}
			}
		}

		// Extract geo data
		List<Place> result = new ArrayList<Place>();
		for (Statement stmt : statements) {
			Activity place = (Activity) stmt.getObject();
			String timestamp = stmt.getTimestamp();

			HashMap<String, JsonElement> extensions = place.getDefinition().getExtensions();
			JsonElement geoJson = extensions.get("http://id.tincanapi.com/extension/geojson");
			JsonElement accuracy = extensions.get("http://id.tincanapi.com/extension/geoaccuracy");

			result.add(new Place(new Gson().toJson(geoJson), accuracy.getAsDouble(), timestamp));
		}

		return result;
	}
}
