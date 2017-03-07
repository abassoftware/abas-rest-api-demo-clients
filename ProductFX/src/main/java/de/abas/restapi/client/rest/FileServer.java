/**
 * creation date: Nov 02, 2015
 * first author: marco
 * maintained by: marco
 * <p>
 * (C) Copyright abas Software AG, Karlsruhe, Germany; 2015
 */
package de.abas.restapi.client.rest;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/images")
public class FileServer implements IFileServer
{
//    @GET
//    @Produces("text/plain")
//    public Response getFileInTextFormat(@PathParam("fileName") String fileName)
//    {
//        System.out.println("File requested is : " + fileName);
//         
//        //Put some validations here such as invalid file name or missing file name
//        if(fileName == null || fileName.isEmpty())
//        {
//            ResponseBuilder response = Response.status(Status.BAD_REQUEST);
//            return response.build();
//        }
//         
//        //Prepare a file object with file to return
//        File file = new File("c:/demoTxtFile.txt");
//         
//        ResponseBuilder response = Response.ok((Object) file);
//        response.header("Content-Disposition", "attachment; filename=\"howtodoinjava.txt\"");
//        return response.build();
//    }
//     
//    @GET
//    @Produces("application/pdf")
//    public Response getFileInPDFFormat(@PathParam("fileName") String fileName)
//    {
//        System.out.println("File requested is : " + fileName);
//         
//        //Put some validations here such as invalid file name or missing file name
//        if(fileName == null || fileName.isEmpty())
//        {
//            ResponseBuilder response = Response.status(Status.BAD_REQUEST);
//            return response.build();
//        }
//         
//        //Prepare a file object with file to return
//        File file = new File("c:/demoPDFFile.pdf");
//         
//        ResponseBuilder response = Response.ok((Object) file);
//        response.header("Content-Disposition", "attachment; filename=\"howtodoinjava.pdf\"");
//        return response.build();
//    }
     
    @GET
    @Produces("image/png")
    public Response getFileInPngFormat(@QueryParam("fileName") String fileName)
    {
        //Put some validations here such as invalid file name or missing file name
        if(fileName == null || fileName.isEmpty())
        {
            ResponseBuilder response = Response.status(Status.BAD_REQUEST);
            return response.build();
        }
         
        //Prepare a file object with file to return
        File file = new File("products/product.png");
        
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"products/product.png\"");
        return response.build();
    }
    
}
