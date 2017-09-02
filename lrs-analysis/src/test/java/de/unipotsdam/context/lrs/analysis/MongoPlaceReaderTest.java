package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.PlaceStatement;

public class MongoPlaceReaderTest {

	@Test
	public void shouldReadPlaces() {
		List<PlaceStatement> result = new MongoPlaceReader().readPlacesOf("hgessner");

		assertFalse(result.isEmpty());
	}
}
