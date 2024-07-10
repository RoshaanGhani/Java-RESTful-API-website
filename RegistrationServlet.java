package com.roshaan.movierental.service;

import com.roshaan.movierental.model.RegistrationResponse;
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
import java.sql.SQLException;

@Path("/register")
public class RegistrationServlet {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response registerUser(@FormParam("username") String username,
                                 @FormParam("password") String password,
                                 @FormParam("email") String email) {
        RegistrationResponse response = new RegistrationResponse();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rental", "root", "")) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);
                    statement.setString(2, hashedPassword);
                    statement.setString(3, email);

                    int rows = statement.executeUpdate();
                    if (rows > 0) {
                        response.setSuccess(true);
                        response.setMessage("Registration successful. You can now log in.");
                    } else {
                        response.setSuccess(false);
                        response.setMessage("Registration failed.");
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
                "<h2>" + response.getMessage() + "</h2>";

        if (response.isSuccess()) {
            htmlResponse += "<a href=\"/movie-rental-service/login.html\">Go to Login</a>";
        } else {
            htmlResponse += "<a href=\"/movie-rental-service/registration.html\">Back to Registration</a>";
        }

        htmlResponse += "</body></html>";

        return Response.ok(htmlResponse).build();
    }
}
