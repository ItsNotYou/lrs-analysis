package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class WorkspaceResponse {

	private List<WorkspaceStatement> result;
	private Integer ok;

	public List<WorkspaceStatement> getResult() {
		return result;
	}

	public void setResult(List<WorkspaceStatement> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
