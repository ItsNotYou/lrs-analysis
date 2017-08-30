package de.unipotsdam.context.lrs.analysis;

import static de.unipotsdam.context.lrs.analysis.LRS.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import de.unipotsdam.context.lrs.analysis.data.Account;
import de.unipotsdam.context.lrs.analysis.data.AccountResponse;

public class MongoAccountReader implements Callable<Collection<Account>> {

	private String ldapShortname;

	public MongoAccountReader(String ldapShortname) {
		this.ldapShortname = ldapShortname;
	}

	@Override
	public Collection<Account> call() throws Exception {
		return readAccountsOf();
	}

	public Collection<Account> readAccountsOf() {
		String user = "mailto:" + ldapShortname + "@uni-potsdam.de";

		List<Object> pipeline = new ArrayList<>();
		pipeline.add(map("$match", map("statement.actor.mbox", user, "statement.object.definition.type", "http://activitystrea.ms/schema/1.0/service", "voided", false)));
		pipeline.add(map("$project", map("_id", 0, "provider", "$statement.object.id", "account", "$statement.object.definition.extensions.http://id&46;tincanapi&46;com/extension/account")));

		AccountResponse result = new LRS().query(pipeline, AccountResponse.class);
		return asUnique(result.getResult());
	}

	private Collection<Account> asUnique(Collection<Account> accounts) {
		return new HashSet<>(accounts);
	}
}
