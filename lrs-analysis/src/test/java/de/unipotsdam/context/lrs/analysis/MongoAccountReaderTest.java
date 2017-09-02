package de.unipotsdam.context.lrs.analysis;

import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Test;

import de.unipotsdam.context.lrs.analysis.data.Account;

public class MongoAccountReaderTest {

	@Test
	public void shouldReadAccounts() throws Exception {
		Collection<Account> result = new MongoAccountReader("hgessner").call();
		assertFalse(result.isEmpty());
	}
}
