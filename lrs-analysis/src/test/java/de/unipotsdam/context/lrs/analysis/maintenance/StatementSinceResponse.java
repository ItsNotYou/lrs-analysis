package de.unipotsdam.context.lrs.analysis.maintenance;

import java.util.List;

public class StatementSinceResponse {

	private List<StatementId> result;
	private Integer ok;

	public List<StatementId> getResult() {
		return result;
	}

	public void setResult(List<StatementId> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
