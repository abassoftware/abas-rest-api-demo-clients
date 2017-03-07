/**
 * creation date: Nov 02, 2015
 * first author: marco
 * maintained by: marco
 * <p>
 * (C) Copyright abas Software AG, Karlsruhe, Germany; 2015
 */
package de.abas.restapi.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public class ClientCreator {

    public static ResteasyClient createClientWithBasicAuthentication(String host, int port, UsernamePasswordCredentials credentials) {
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(host, port), credentials);
        final ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient, new BasicHttpContext());

        return new ResteasyClientBuilder().httpEngine(engine).build();
    }
}
