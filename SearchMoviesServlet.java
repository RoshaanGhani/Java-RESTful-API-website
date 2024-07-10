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

@Path("/search")
public class SearchMoviesServlet {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response searchMovies(@QueryParam("title") String title, @QueryParam("genre") String genre) {
        List<Movies> moviesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rental", "root", "")) {
            StringBuilder query = new StringBuilder("SELECT * FROM movies WHERE 1=1");
            if (title != null && !title.isEmpty()) {
                query.append(" AND title LIKE ?");
            }
            if (genre != null && !genre.isEmpty()) {
                query.append(" AND genre = ?");
            }
            PreparedStatement statement = connection.prepareStatement(query.toString());
            int paramIndex = 1;
            if (title != null && !title.isEmpty()) {
                statement.setString(paramIndex++, "%" + title + "%");
            }
            if (genre != null && !genre.isEmpty()) {
                statement.setString(paramIndex++, genre);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
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

        // Construct the HTML response
        StringBuilder htmlResponse = new StringBuilder("<html><body><h1>Search Results</h1>");
        if (moviesList.isEmpty()) {
            htmlResponse.append("<p>No movies found</p>");
        } else {
            for (Movies movie : moviesList) {
                htmlResponse.append("<div>")
                            .append("<h2>").append(movie.getTitle()).append("</h2>")
                            .append("<p>").append(movie.getGenre()).append("</p>")
                            .append("<p>").append(movie.getDescription()).append("</p>")
                            .append("</div>");
            }
        }
        htmlResponse.append("<a href=\"/movie-rental-service/search.html\">Back to Search</a></body></html>");
        return Response.ok(htmlResponse.toString()).build();
    }
}
