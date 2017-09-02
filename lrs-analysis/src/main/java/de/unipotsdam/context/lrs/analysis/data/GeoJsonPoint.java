package de.unipotsdam.context.lrs.analysis.data;

import java.util.List;

public class GeoJsonPoint {

	private GeoPoint geometry;

	public GeoPoint getGeometry() {
		return geometry;
	}

	public void setGeometry(GeoPoint geometry) {
		this.geometry = geometry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	private String type;
	private List<String> properties;
}
