package com.roshaan.movierental.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/home")
public class MainServlet {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response home() {
        return Response.ok("Welcome to the Movie Rental Service").build();
    }
}
