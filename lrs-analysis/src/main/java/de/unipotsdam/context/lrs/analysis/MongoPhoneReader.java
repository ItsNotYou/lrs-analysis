package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import de.unipotsdam.context.lrs.analysis.data.PhoneNumberResponse;
import de.unipotsdam.context.lrs.analysis.data.PhoneStatement;

public class MongoPhoneReader implements Callable<Collection<String>> {

	private String ldapShortname;

	public MongoPhoneReader(String ldapShortname) {
		this.ldapShortname = ldapShortname;
	}

	/**
	 * Reads phone numbers of {@link #ldapShortname}
	 */
	@Override
	public Collection<String> call() throws Exception {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.object.definition.type", "http://activitystrea.ms/schema/1.0/device", "voided", false)));
		pipeline.add(map("$group", map("_id", "$statement.object.definition.extensions.http://id&46;tincanapi&46;com/extension/phonenumbers")));

		PhoneNumberResponse response = new LRS().query(pipeline, PhoneNumberResponse.class);
		return asPhoneNumbers(response.getResult());
	}

	private Collection<String> asPhoneNumbers(List<PhoneStatement> statements) {
		Collection<String> result = new HashSet<>();

		for (PhoneStatement stmt : statements) {
			if (stmt.get_id() == null)
				continue;

			for (String pn : stmt.get_id()) {
				result.add(pn);
			}
		}

		return result;
	}
}
