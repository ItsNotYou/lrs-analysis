package de.unipotsdam.context.lrs.analysis.data;

public class Account {

	private String provider;
	private String account;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		return this.provider.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			Account other = (Account) obj;
			boolean isProviderEqual = getProvider() == null ? other.getProvider() == null : getProvider().equals(other.getProvider());
			boolean isAccountEqual = getAccount() == null ? other.getAccount() == null : getAccount().equals(other.getAccount());
			return isProviderEqual && isAccountEqual;
		} else {
			return false;
		}
	}
}
