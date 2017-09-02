package de.unipotsdam.context.lrs.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.uri.UriComponent;

import com.google.gson.Gson;

import gov.adlnet.xapi.util.Base64;

public class LRS {

	private static final String lrsUrl = "http://lrs.soft.cs.uni-potsdam.de/";
	private static final String username = "f1e520976fb3cd27127bef0bfd2c4af924bfd2fc";
	private static final String password = "b4f0955aea62c4d9f94a98e32a400e665f7338a7";

	public <T> T query(List<Object> pipeline, Class<T> entityType) {
		String pipeJson = asJson(pipeline);

		String loginData = username + ":" + password;
		String login = ("Basic " + Base64.encodeToString(loginData.getBytes(), Base64.DEFAULT)).replace("\n", "");

		Client client = ClientBuilder.newClient();
		Response response = client.target(lrsUrl).path("/api/v1/statements/aggregate").queryParam("pipeline", pipeJson).request().header("Authorization", login).get();
		return response.readEntity(entityType);
	}

	public static Map<String, Object> map(Object... keyValuePairs) {
		Map<String, Object> result = new HashMap<>();
		for (int count = 0; count < keyValuePairs.length; count += 2) {
			result.put((String) keyValuePairs[count], keyValuePairs[count + 1]);
		}
		return result;
	}

	private String asJson(Object object) {
		String json = new Gson().toJson(object);
		return UriComponent.contextualEncode(json, UriComponent.Type.QUERY_PARAM, false);
	}
}
