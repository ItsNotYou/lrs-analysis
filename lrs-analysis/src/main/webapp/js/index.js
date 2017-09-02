function initMap() {
	// Focus Potsdam
	mymap = L.map('mapid').setView([ 52.3952188, 13.1016003 ], 12);

	// Add satellite view
	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiaGdlc3NuZXIiLCJhIjoiY2o1d2o5aDJ5MGVsMjMzb2d3bzN4MzMwaiJ9.cUb5rM2lPWE2lw-mLk2ofQ', {
		attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		maxZoom : 18,
		id : 'mapbox.streets',
		accessToken : 'your.mapbox.access.token'
	}).addTo(mymap);

	$.ajax("api/places?ldapShortname=hgessner", {
		success : showPlaces,
		error : error
	});
}

function showPlaces(data) {
	_.each(data.places, function(place) {
		var geojsonMarkerOptions = {
			//radius : place.accuracy,
			radius: 50,
			fillColor : "#ff7800",
			color : "#000",
			weight : 1,
			opacity : 1,
			fillOpacity : 0.5
		};

		L.geoJSON(place.geojson, {
			pointToLayer : function(feature, latlng) {
				return L.circle(latlng, geojsonMarkerOptions);
			}
		}).addTo(mymap);
	});
}

function error() {
	alert("Fehler beim Abruf der Daten");
	console.error(arguments);
}

$(initMap);
