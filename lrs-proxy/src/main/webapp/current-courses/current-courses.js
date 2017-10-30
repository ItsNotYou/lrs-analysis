// Type definitions

/**
 * @callback CourseLoaderCallback
 * Receives currently attended courses
 * @param {Array<{groupId: Integer}>} courses - Currently attended courses
 */


// Implementations

/**
 * Loads currently attended courses of a user and matches them to a given set of Liferay workspaces
 * @param user
 * @param {Array<{groupId: Integer}>} groups
 * @param {CourseLoaderCallback} loadCallback
 */
function loadCurrentCourses(user, groups, loadCallback) {
	var selectCourses = function(groups, courses) {
		// Run through all currently attended courses
		return _.chain(courses.courses)
			.map(function(course) {
				// Find the matching Liferay workspace
				var groupId = parseInt(course.groupId, 10);
				return _.findWhere(groups, {groupId: groupId});
			})
			// Discard empty matches
			.filter(function(group) { return group; })
			.value();
	};
	
	var retrieveCurrentUserCourses = function() {
		// Retrieve current user courses
		var url = "/lrs-analysis/api/users/" + encodeURIComponent(user) + "/courses/current";
		$.ajax(url, {
			error: function(jqXHR, textStatus, errorThrown) {
				console.error(textStatus, errorThrown);
				loadCallback([]);
			},
			success: function(courses) {
				// Map response to known groups
				var selectedCourses = selectCourses(groups, courses);
				loadCallback(selectedCourses);
			}
		});
	};
	
	// Execute now and repeat every 30 seconds
	retrieveCurrentUserCourses();
	setInterval(retrieveCurrentUserCourses, 30000);
}
