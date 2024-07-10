package com.roshaan.movierental.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Path("/testdb")
public class DatabaseConnectionTestServlet {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testDatabaseConnection() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/movie_rental";
        String jdbcUser = "root";
        String jdbcPassword = "";

        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
                if (connection != null) {
                    return Response.ok("{\"message\":\"Database connection successful\"}").build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"message\":\"Failed to establish database connection\"}").build();
                }
            }
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"JDBC Driver not found: " + e.getMessage() + "\"}").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Database connection error: " + e.getMessage() + "\"}").build();
        }
    }
}
