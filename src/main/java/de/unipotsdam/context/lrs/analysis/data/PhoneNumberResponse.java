package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class PhoneNumberResponse {

	private List<PhoneStatement> result;
	private Integer ok;

	public List<PhoneStatement> getResult() {
		return result;
	}

	public void setResult(List<PhoneStatement> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
