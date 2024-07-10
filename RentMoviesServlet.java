package com.roshaan.movierental.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Path("/rent")
public class RentMoviesServlet {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rentMovie(@FormParam("userId") int userId, @FormParam("movieId") int movieId) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/movie_rental";
        String jdbcUser = "root";
        String jdbcPassword = "";

        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
                 PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO rentals (user_id, movie_id, rental_date) VALUES (?, ?, NOW())")) {
                statement.setInt(1, userId);
                statement.setInt(2, movieId);

                int rows = statement.executeUpdate();
                if (rows > 0) {
                    return Response.ok("{\"message\":\"Movie rented successfully\"}").build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"message\":\"Failed to rent movie\"}").build();
                }
            }
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"JDBC Driver not found: " + e.getMessage() + "\"}").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"SQL Error: " + e.getMessage() + "\"}").build();
        }
    }
}
