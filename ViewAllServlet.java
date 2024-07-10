package com.roshaan.movierental.service;

import com.roshaan.movierental.model.Movies;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Path("/movies")
public class ViewAllServlet {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllMovies() {
        List<Movies> moviesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rental", "root", "")) {
            String query = "SELECT * FROM movies";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) { // iterates through the movies
                Movies movie = new Movies();
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDescription(resultSet.getString("description"));
                movie.setReleaseDate(resultSet.getString("release_date"));
                movie.setRating(resultSet.getFloat("rating"));
                moviesList.add(movie);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
        return Response.ok(moviesList).build();
    }
}
