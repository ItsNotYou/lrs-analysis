package de.unipotsdam.context.lrs.analysis.data;

public class Place {

	private String geoJson;
	private double accuracy;
	private String timestamp;

	public Place() {
	}

	public Place(String geoJson, double accuracy, String timestamp) {
		this.geoJson = geoJson;
		this.accuracy = accuracy;
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getGeoJson() {
		return geoJson;
	}

	public void setGeoJson(String geoJson) {
		this.geoJson = geoJson;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
}
