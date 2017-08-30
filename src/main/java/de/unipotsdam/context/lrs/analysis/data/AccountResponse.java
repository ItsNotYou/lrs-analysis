package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class AccountResponse {

	private List<Account> result;
	private Integer ok;

	public List<Account> getResult() {
		return result;
	}

	public void setResult(List<Account> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
