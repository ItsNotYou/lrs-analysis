package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.util.ArrayList;
import java.util.List;

import de.unipotsdam.context.lrs.analysis.data.PlaceStatement;
import de.unipotsdam.context.lrs.analysis.data.PlacesResponse;

public class MongoPlaceReader {

	public List<PlaceStatement> readPlacesOf(String ldapShortname) {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.verb.id", "http://activitystrea.ms/schema/1.0/at", "voided", false)));
		pipeline.add(map("$project", map("_id", 0, "geojson", "$statement.object.definition.extensions.http://id&46;tincanapi&46;com/extension/geojson", "accuracy", "$statement.object.definition.extensions.http://id&46;tincanapi&46;com/extension/geoaccuracy")));

		PlacesResponse result = new LRS().query(pipeline, PlacesResponse.class);

		return result.getResult();
	}
}
