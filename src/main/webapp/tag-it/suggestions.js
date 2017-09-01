function loadProfileSuggestions(user, successCallback) {
	var url = "/lrs-analysis/api/users/" + user + "/profile";
	$.ajax(url, {
		success: successCallback,
		error: function(jqXHR, textStatus, errorThrown) { console.error(textStatus, errorThrown); }
	});
}

function createSuggestionsTag(entrys, tagitElement, clickCallback) {
	$(document).ready(function() {
		$.each(entrys, function(index, entry) {
			tagitElement.append("<li>" + entry + "</li>");
		});
		
        tagitElement.tagit({
			readOnly: true,
			onTagClicked: clickCallback
		});
    });
}

/**
 * Maps a server response to labeled data. The returned objects may contain arbitrary fields, but a label field is required
 * @callback ResponseMapper
 * @param {*} LRS analysis response
 * @returns {Array<{label: String}>}
 */

/**
 * 
 * @param user
 * @param tagitElement
 * @param {ResponseMapper} responseMapper
 * @param addCallback
 * @returns
 */
function loadSuggestions(user, tagitElement, responseMapper, addCallback) {
	var addLabels = function(labels) {
		$(document).ready(function() {
			// Add labels
			$.each(labels, function(index, entry) {
				var li = $("<li>").text(entry.label);
				tagitElement.append(li);
			});
			
			// Create tagit widget
	        tagitElement.tagit({
				readOnly: true,
				onTagClicked: function(event, ui) {
					// Translate to label entry
					var finds = $.grep(labels, function(label) { return label.label === ui.tagLabel; });
					if (finds.length >= 1) {
						// Notify callback
						addCallback(finds[0]);
					} else {
						// Whoops, there should have been a match
						console.error("Could not find the selected tag", ui.tagLabel);
					}
				}
			});
	    });
	};
	
	// Retrieve user profile
	var url = "/lrs-analysis/api/users/" + encodeURIComponent(user) + "/profile";
	$.ajax(url, {
		error: function(jqXHR, textStatus, errorThrown) { console.error(textStatus, errorThrown); },
		success: function(data) {
			// Map response to labeled array
			var labels = responseMapper(data);
			addLabels(labels);
		}
	});
}
