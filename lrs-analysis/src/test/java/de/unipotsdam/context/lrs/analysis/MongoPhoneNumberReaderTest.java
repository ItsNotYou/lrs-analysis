package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Test;

public class MongoPhoneNumberReaderTest {

	@Test
	public void shouldReadPhoneNumbers() throws Exception {
		Collection<String> result = new MongoPhoneReader("hgessner").call();
		assertFalse(result.isEmpty());
	}
}
