package de.unipotsdam.context.lrs.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import de.unipotsdam.context.lrs.analysis.data.CurrentlyAttendedCoursesResponse;
import de.unipotsdam.context.lrs.analysis.data.ProfileResponse;
import de.unipotsdam.context.lrs.analysis.filter.ProfileFilter;
import de.unipotsdam.context.lrs.analysis.filter.RunningCoursesFilter;

@Path("/users")
public class AnalysisResource {

	private static final Logger log = Logger.getLogger(AnalysisResource.class.getName());

	@GET
	@Path("/{user}/courses/current")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public CurrentlyAttendedCoursesResponse getCurrentlyAttendedCourses(@PathParam("user") String ldapShortname) {
		try (RunningCoursesFilter courses = new RunningCoursesFilter()) {
			return courses.getCurrentlyAttendedCourses(ldapShortname);
		} catch (Exception e) {
			log.log(Level.INFO, "Could not read current courses for " + ldapShortname, e);
			throw new WebApplicationException(e, 500);
		}
	}

	@GET
	@Path("/{user}/profile")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public ProfileResponse getProfile(@PathParam("user") String ldapShortname) {
		try (ProfileFilter profiles = new ProfileFilter()) {
			return profiles.getProfile(ldapShortname);
		} catch (Exception e) {
			log.log(Level.INFO, "Could not read profile for " + ldapShortname, e);
			throw new WebApplicationException(e, 500);
		}
	}
}
