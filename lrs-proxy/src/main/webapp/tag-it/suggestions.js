// Type definitions

/**
 * @callback ResponseMapper
 * Maps a server response to labeled data. The returned objects may contain arbitrary fields, but a label field is required
 * @param {*} tags - LRS analysis response
 * @returns {Array<{label: String}>}
 */

/**
 * @callback TagAddCallback
 * Handles a tag add event. The given tag content may contain arbitrary fields defined in the associated ResponseMapper
 * @param {Array<{label: String}>} tag - Tag content
 * @see ResponseMapper
 */


// Implementations

var blacklist = ["tenoriof", "matweise", "dehne", "vwoelfer", "mazandar", "moebert", "zeisse", "morgiel"];

/**
 * 
 * @param user
 * @param tagitElement
 * @param {ResponseMapper} responseMapper
 * @param addCallback
 * @returns
 */
function loadSuggestions(user, tagitElement, responseMapper, addCallback) {
	// Skip blacklisted users
	if (blacklist.indexOf(user) !== -1) {
		// Remove suggestions label and list
		$(document).ready(function() {
			var id = tagitElement.attr("id");
			tagitElement.remove();
			$("label[for=" + id + "]").remove();
		});
		return;
	}
	
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
