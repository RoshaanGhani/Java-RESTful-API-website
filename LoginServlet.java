package com.roshaan.movierental.service;

import com.roshaan.movierental.model.LoginResponse;
import org.mindrot.jbcrypt.BCrypt;
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
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("/login")
public class LoginServlet {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response loginUser(@FormParam("username") String username,
                              @FormParam("password") String password) {
        LoginResponse response = new LoginResponse();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rental", "root", "")) {
                String query = "SELECT * FROM users WHERE username = ?";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String storedHashedPassword = resultSet.getString("password");

                            if (BCrypt.checkpw(password, storedHashedPassword)) {
                                response.setSuccess(true);
                                response.setUserId(resultSet.getInt("id"));
                                // Redirect to home page
                                return Response.seeOther(java.net.URI.create("/movie-rental-service/home.html")).build();
                            } else {
                                response.setSuccess(false);
                                response.setMessage("Invalid credentials");
                            }
                        } else {
                            response.setSuccess(false);
                            response.setMessage("Invalid credentials");
                        }
                    }
                }

            } catch (SQLException e) {
                response.setSuccess(false);
                response.setMessage("Error: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("JDBC Driver not found: " + e.getMessage());
        }

        String htmlResponse = "<html><body>" +
                "<h2>" + response.getMessage() + "</h2>" +
                "<a href=\"/movie-rental-service/login.html\">Back to Login</a>" +
                "</body></html>";
        return Response.ok(htmlResponse).build();
    }
}
