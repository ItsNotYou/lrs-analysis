package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class PlacesResponse {

	private List<PlaceStatement> result;
	private Integer ok;

	public List<PlaceStatement> getResult() {
		return result;
	}

	public void setResult(List<PlaceStatement> result) {
		this.result = result;
	}

	public Integer getOk() {
		return ok;
	}

	public void setOk(Integer ok) {
		this.ok = ok;
	}
}
