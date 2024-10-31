/*
 * File name: CreateAccountServlet.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This servlet processes requests to create new user accounts.
 */
package com.bookkeeper.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bookkeeper.database.DatabaseConnection;

@WebServlet("/createAccount")
public class CreateAccountServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (password.equals(confirmPassword)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                response.sendRedirect("index.html");
            } catch (SQLException e) {
                response.getWriter().write("Error creating account: " + e.getMessage());
            }
        } else {
            response.getWriter().write("Passwords do not match. Please try again.");
        }
    }
}
