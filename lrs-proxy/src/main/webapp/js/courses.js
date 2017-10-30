function initForm() {
	$("form#associater").submit(onAssociate);
	
	// Init xAPI wrapper
    var conf = {
        endpoint: "http://lrs.soft.cs.uni-potsdam.de/data/xAPI/",
        auth: "Basic " + toBase64('f1e520976fb3cd27127bef0bfd2c4af924bfd2fc:b4f0955aea62c4d9f94a98e32a400e665f7338a7'),
        strictCallbacks: true
    };
    ADL.XAPIWrapper.changeConfig(conf);
}

function onAssociate(ev) {
	ev.preventDefault();
	
	// Read data
	var semester = $("input#semester").val();
	var courseId = $("input#courseId").val();
	var groupId = $("input#groupId").val();
	var userId = $("input#userId").val();
	
	// Check for missing values
	if (!semester) {
		alert("Semester required");
		return;
	}
	if (!courseId) {
		alert("Course ID required");
		return;
	}
	if (!groupId) {
		alert("Group ID required");
		return;
	}
	if (!userId) {
		alert("User ID required");
		return;
	}
	
	// Create statement
	var stmt = new ADL.XAPIStatement(
		new ADL.XAPIStatement.Agent('mailto:' + userId + '@uni-potsdam.de', userId),
		{
			id: "http://activitystrea.ms/schema/1.0/attach",
			display: {
				"en-US": "attached"
			}
		},
		{
			id: "http://xapi.uni-potsdam.de/workspace/" + groupId,
			definition: {
				name: {
					"en-US": "Workspace " + groupId
				},
				description: {
					"en-US": "Represents a workspace in a PLE."
				},
				type: "http://xapi.uni-potsdam.de/ple/workspace"
			},
			objectType: "Activity"
		}
	);
	stmt.context = {
		contextActivities: {
			parent: [
				{
					id: "http://xapi.uni-potsdam.de/event/" + semester + "/" + courseId,
					objectType: "Activity"
				}
			],
            category: [
                {
                    id: "http://lrs.uni-potsdam.de/lrs-proxy",
                    definition: {
                        type: "http://id.tincanapi.com/activitytype/source"
                    }
                }
            ]
        }
    };
	
	// Send statement
	ADL.XAPIWrapper.sendStatement(stmt, function(err, res, body) {
		if (err) {
			alert("Fehler: " + JSON.stringify(err));
		} else {
			alert("Zuordnung erfolgreich");
		}
	});
}

$(initForm);
