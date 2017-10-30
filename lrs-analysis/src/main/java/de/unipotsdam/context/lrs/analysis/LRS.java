package de.unipotsdam.context.lrs.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.uri.UriComponent;

import com.google.gson.Gson;

import gov.adlnet.xapi.util.Base64;

public class LRS {

	private final String lrsUrl;
	private final String username;
	private final String password;

	public LRS() throws IOException {
		// Get LRS configuration
		Properties properties = new Properties();
		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("lrs.properties")) {
			properties.load(input);
		}

		// Load necessary properties
		this.lrsUrl = properties.getProperty("lrs.url");
		this.username = properties.getProperty("lrs.username");
		this.password = properties.getProperty("lrs.password");
	}

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
