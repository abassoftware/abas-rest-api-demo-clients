/**
 * creation date: Nov 02, 2015
 * first author: marco
 * maintained by: marco
 * <p>
 * (C) Copyright abas Software AG, Karlsruhe, Germany; 2015
 */
package de.abas.restapi.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/obj/data/0:1/")
public interface Customers {

	@GET
	@Path("{id}")
	@Produces("application/abas.objects+json")
	String withId(@PathParam("id")String id);

	@GET
	@Path("{id}")
	@Produces("application/abas.objects+json")
	String withId(@PathParam("id")String id, @QueryParam("headFields")String headFields);

	@GET
	@Produces("application/abas.objects+json")
	String customerList(@QueryParam("criteria")String criteria);

	@GET
	@Produces("application/abas.objects+json")
	String customerList(@QueryParam("criteria")String criteria, @QueryParam("headFields")String headFields);

}
