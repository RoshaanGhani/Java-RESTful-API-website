package com.roshaan.movierental.service;

import com.roshaan.movierental.model.Movies;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Path("/rentals")
public class ViewRentedMoviesServlet {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewRentedMovies(@QueryParam("userId") int userId) {
        List<Movies> rentedMoviesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rental", "root", "")) {
            String query = "SELECT m.* FROM movies m JOIN rentals r ON m.id = r.movie_id WHERE r.user_id = ?";
            //The query retrieves all columns (m.*) from the movies table for movies that have been rented by a specific user. It does this by joining the movies table with the rentals table on the condition that the id in the movies table matches the movie_id in the rentals table. 
            //The WHERE clause ensures that only rentals by the specified user (indicated by user_id) are included in the results. The actual user ID will be provided as a parameter when the query is executed.
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Movies movie = new Movies();
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setGenre(resultSet.getString("genre"));
                movie.setDescription(resultSet.getString("description"));
                movie.setReleaseDate(resultSet.getString("release_date"));
                movie.setRating(resultSet.getFloat("rating"));
                rentedMoviesList.add(movie);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: " + e.getMessage()).build();
        }
        return Response.ok(rentedMoviesList).build();
    }
}
