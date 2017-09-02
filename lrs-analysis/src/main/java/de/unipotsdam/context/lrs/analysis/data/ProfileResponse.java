package de.unipotsdam.context.lrs.analysis.data;

import java.util.Collection;

public class ProfileResponse {

	private Collection<String> languages;
	private Collection<Account> accounts;
	private Collection<String> phoneNumbers;
	private Collection<Course> attendedCourses;

	public Collection<String> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<String> languages) {
		this.languages = languages;
	}

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}

	public Collection<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Collection<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Collection<Course> getAttendedCourses() {
		return attendedCourses;
	}

	public void setAttendedCourses(Collection<Course> attendedCourses) {
		this.attendedCourses = attendedCourses;
	}
}
