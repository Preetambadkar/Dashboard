//package com.ex.servlet;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Date;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//
//    private static final String SECRET_KEY = "your_secret_key";
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tdtldb", "root", "root")) {
//            String query = "SELECT password FROM users WHERE username = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                stmt.setString(1, username);
//                try (ResultSet rs = stmt.executeQuery()) {
//                    if (rs.next()) {
//                        String storedPassword = rs.getString("password");
//                        if (storedPassword.equals(password)) { // Remember to check hashed passwords in a real application
//                            String token = Jwts.builder()
//                                    .setSubject(username)
//                                    .setIssuedAt(new Date())
//                                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
//                                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                                    .compact();
//
//                            response.setContentType("application/json");
//                            PrintWriter out = response.getWriter();
//                            out.print("{\"token\":\"" + token + "\"}");
//                            out.flush();
//                        } else {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
//                        }
//                    } else {
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error logging in user");
//        }
//    }
//}

package com.ex.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SECRET_KEY = "your_secret_key"; 
    ConnectDb db = new ConnectDb();
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        db.loadDriver(db.getDbDriver());
        // Debugging output
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and password are required.");
            return;
        }

        try (Connection conn = db.getConnection()) {
            String query = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("PASSWORD");
                        if (BCrypt.checkpw(password, hashedPassword)) {
                            // Generate JWT token
                            String token = Jwts.builder()
                                    .setSubject(username)
                                    .setIssuedAt(new Date())
                                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                                    .compact();

                            response.setContentType("application/json");
                            PrintWriter out = response.getWriter();
                            out.print("{\"token\":\"" + token + "\"}");
                            out.flush();
                        } else {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error logging in");
        }
    }
}




