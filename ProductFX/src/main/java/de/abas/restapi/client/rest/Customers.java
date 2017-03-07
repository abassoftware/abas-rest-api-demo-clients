/**
 * creation date: Nov 02, 2015
 * first author: marco
 * maintained by: marco
 * <p>
 * (C) Copyright abas Software AG, Karlsruhe, Germany; 2015
 */
package de.abas.restapi.client.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.abas.restapi.client.I18N;

@Path("/obj/data/0:1/")
public interface Customers {

	@GET
	@Path("{id}")
	@Produces("application/abas.objects+json")
	String withId(@PathParam("id")String id, @DefaultValue(I18N.LANG) @QueryParam("variableLanguage") String variableLanguage);
	
	@GET
	@Path("{id}")
	@Produces("application/abas.objects+json")
	String withId(@PathParam("id")String id, @QueryParam("headFields")String headFields, @DefaultValue(I18N.LANG) @QueryParam("variableLanguage") String variableLanguage);
	
	@GET
	@Produces("application/abas.objects+json")
	String customerList(@QueryParam("criteria")String criteria, @DefaultValue(I18N.LANG) @QueryParam("variableLanguage") String variableLanguage);

	@GET
	@Produces("application/abas.objects+json")
	String customerList(@QueryParam("criteria")String criteria, @QueryParam("headFields")String headFields, @DefaultValue(I18N.LANG) @QueryParam("variableLanguage") String variableLanguage);
	
}
