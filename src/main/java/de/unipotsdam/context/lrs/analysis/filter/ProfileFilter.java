package de.unipotsdam.context.lrs.analysis.filter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.unipotsdam.context.lrs.analysis.MongoAccountReader;
import de.unipotsdam.context.lrs.analysis.MongoCourseReader;
import de.unipotsdam.context.lrs.analysis.MongoLanguageReader;
import de.unipotsdam.context.lrs.analysis.MongoPhoneReader;
import de.unipotsdam.context.lrs.analysis.data.Account;
import de.unipotsdam.context.lrs.analysis.data.Course;
import de.unipotsdam.context.lrs.analysis.data.ProfileResponse;

public class ProfileFilter implements Closeable {

	private ExecutorService threadPool;

	public ProfileFilter() {
		this.threadPool = Executors.newFixedThreadPool(8);
	}

	public ProfileResponse getProfile(final String ldapShortname) throws InterruptedException, ExecutionException {
		Future<Collection<String>> languages = threadPool.submit(new MongoLanguageReader(ldapShortname));
		Future<Collection<Account>> accounts = threadPool.submit(new MongoAccountReader(ldapShortname));
		Future<Collection<Course>> courses = threadPool.submit(new MongoCourseReader(ldapShortname));
		Future<Collection<String>> phoneNumbers = threadPool.submit(new MongoPhoneReader(ldapShortname));

		ProfileResponse result = new ProfileResponse();
		result.setLanguages(languages.get());
		result.setAccounts(accounts.get());
		result.setAttendedCourses(courses.get());
		result.setPhoneNumbers(phoneNumbers.get());
		return result;
	}

	@Override
	public void close() throws IOException {
		threadPool.shutdown();
	}
}
