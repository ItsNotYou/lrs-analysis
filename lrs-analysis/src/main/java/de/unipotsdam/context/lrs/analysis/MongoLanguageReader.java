package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import de.unipotsdam.context.lrs.analysis.data.LanguageResponse;
import de.unipotsdam.context.lrs.analysis.data.LanguageStatement;

public class MongoLanguageReader implements Callable<Collection<String>> {

	private List<String> validLanguages;
	private String ldapShortname;

	public MongoLanguageReader(String ldapShortname) {
		this.validLanguages = Arrays.asList(Locale.getISOLanguages());
		this.ldapShortname = ldapShortname;
	}

	@Override
	public Collection<String> call() throws Exception {
		return readLanguagesOf();
	}

	public Collection<String> readLanguagesOf() {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.context.language")));

		LanguageResponse response = new LRS().query(pipeline, LanguageResponse.class);
		return asDisplayLanguages(response.getResult(), Locale.GERMAN);
	}

	private Collection<String> asDisplayLanguages(List<LanguageStatement> statements, Locale hostLanguage) {
		Collection<String> result = new HashSet<>();

		for (LanguageStatement stmt : statements) {
			if (stmt.get_id() == null)
				continue;

			Locale locale = Locale.forLanguageTag(stmt.get_id());
			String language = locale.getLanguage();
			if (validLanguages.contains(language)) {
				result.add(locale.getDisplayLanguage(hostLanguage));
			}
		}

		return result;
	}
}
