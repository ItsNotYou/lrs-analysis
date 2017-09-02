package de.unipotsdam.context.lrs.analysis;

import java.io.IOException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.unipotsdam.context.lrs.analysis.data.Places;

@Path("/places")
public class PlaceResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Places getPlaces(@QueryParam(value = "ldapShortname") String ldapShortname) throws IOException {
		if (ldapShortname == null || ldapShortname.isEmpty()) {
			throw new BadRequestException("ldapShortname is required");
		}

		try {
			Places result = new Places();
			result.setPlaces(new MongoPlaceReader(ldapShortname).call());
			return result;
		} catch (Exception ex) {
			throw new InternalServerErrorException("Could not read from LRS", ex);
		}
	}
}
