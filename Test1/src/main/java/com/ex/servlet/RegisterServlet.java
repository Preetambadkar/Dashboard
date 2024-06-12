package com.ex.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import com.java.secure.User;
import com.java.secure.UserStore;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("message", "Username and password must not be empty");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        ConnectDb db = new ConnectDb();
        db.loadDriver(db.getDbDriver());
        Connection con = db.getConnection();

        try {
            String checkUserQuery = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkUserStmt = con.prepareStatement(checkUserQuery);
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();

            if (rs.next()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.put("message", "User already exists");
                out.print(jsonResponse.toString());
                out.flush();
                rs.close();
                checkUserStmt.close();
                return;
            }
            rs.close();
            checkUserStmt.close();

            String addUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement addUserStmt = con.prepareStatement(addUserQuery);
            addUserStmt.setString(1, username);
            addUserStmt.setString(2, hashedPassword); // Insert hashed password
            addUserStmt.executeUpdate();
            addUserStmt.close();

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("message", "User registered successfully");
            out.print(jsonResponse.toString());
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("message", "Error registering user");
            out.print(jsonResponse.toString());
            out.flush();
        } finally {
            db.closeConnection(con);
        }
    }
}
