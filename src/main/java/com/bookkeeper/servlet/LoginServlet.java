package com.bookkeeper.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.bookkeeper.database.DatabaseConnection;


public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        

                        HttpSession session = request.getSession();
                        session.setAttribute("userId", userId);
                        
                        response.sendRedirect("submit-data.html"); // Redirect to main page after login
                    } else {
                        response.sendRedirect("login.html?error=Invalid credentials");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=An error occurred");
        }
    }
}
