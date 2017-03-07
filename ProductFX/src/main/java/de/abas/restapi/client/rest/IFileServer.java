package de.abas.restapi.client.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/images")
public interface IFileServer {
    @GET
    @Produces("image/png")
    public Response getFileInPngFormat(@QueryParam("fileName") String fileName);

}
