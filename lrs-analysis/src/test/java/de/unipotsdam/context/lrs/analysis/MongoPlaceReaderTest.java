package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.PlaceStatement;

public class MongoPlaceReaderTest {

	@Test
	public void shouldReadPlaces() throws Exception {
		Collection<PlaceStatement> result = new MongoPlaceReader("hgessner").call();
		assertFalse(result.isEmpty());
	}
}
