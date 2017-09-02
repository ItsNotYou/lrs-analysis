package de.unipotsdam.context.lrs.analysis.data;

public class PlaceStatement {

	private GeoJsonPoint geojson;
	private Double accuracy;

	public GeoJsonPoint getGeojson() {
		return geojson;
	}

	public void setGeojson(GeoJsonPoint geojson) {
		this.geojson = geojson;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}
}
