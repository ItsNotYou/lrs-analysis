package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class LanguageResponse {

	private List<LanguageStatement> result;
	private Integer ok;

	public List<LanguageStatement> getResult() {
		return result;
	}

	public void setResult(List<LanguageStatement> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
