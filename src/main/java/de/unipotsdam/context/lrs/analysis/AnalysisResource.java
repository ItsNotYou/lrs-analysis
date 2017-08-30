package de.unipotsdam.context.lrs.analysis;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import de.unipotsdam.context.lrs.analysis.data.CourseEvent;
import de.unipotsdam.context.lrs.analysis.data.CurrentlyAttendedCoursesResponse;
import de.unipotsdam.context.lrs.analysis.data.ProfileResponse;
import de.unipotsdam.context.lrs.analysis.filter.CoursesFilter;
import de.unipotsdam.context.lrs.analysis.filter.ProfileFilter;

@Path("/users")
public class AnalysisResource {

	@GET
	@Path("/courses/current")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public CurrentlyAttendedCoursesResponse getCurrentlyAttendedCourses(@QueryParam("ldapShortname") String ldapShortname) {
		List<CourseEvent> result = new CoursesFilter().getCurrentlyAttendedCourses(ldapShortname);
		return new CurrentlyAttendedCoursesResponse(result);
	}

	@GET
	@Path("/{user}/profile")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ProfileResponse getProfile(@PathParam("user") String ldapShortname) {
		try {
			return new ProfileFilter().getProfile(ldapShortname);
		} catch (Exception e) {
			throw new WebApplicationException("Could not read data from data source", 500);
		}
	}
}
