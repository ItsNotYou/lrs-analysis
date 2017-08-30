package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;

import jersey.repackaged.com.google.common.collect.Lists;

public class MongoLanguageReaderTest {

	@Test
	public void shouldReadPlaces() {
		Collection<String> result = new MongoLanguageReader("hgessner").readLanguagesOf();

		System.out.println(result);
		assertFalse(result.isEmpty());
	}

	@Test
	public void shouldThrowSomething() {
		Locale result = Locale.forLanguageTag("US");
		result.getLanguage();
		ArrayList<String> valids = Lists.newArrayList(Locale.getISOLanguages());
		String language = result.getLanguage();
		if (valids.contains(language)) {
			System.out.println("Sprache enthalten: " + result.getDisplayLanguage(Locale.GERMAN));
		} else {
			System.out.println("Sprache nicht enthalten");
		}
	}
}
