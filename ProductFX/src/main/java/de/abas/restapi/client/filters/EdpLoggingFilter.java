package de.abas.restapi.client.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

public class EdpLoggingFilter implements ClientRequestFilter {
	String value;

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String edpLoggingHeader = "X-abas-mw-edp-log-level";
		MultivaluedMap<String, Object> mmap = requestContext.getHeaders();
		mmap.put(edpLoggingHeader, Arrays.asList(value));
	}
}