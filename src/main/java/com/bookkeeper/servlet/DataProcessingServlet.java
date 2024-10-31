/* 
 * File name: DataProcessingServlet.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This servlet handles requests for processing financial data, including income and expenses.
 */

package com.bookkeeper.servlet;

import com.bookkeeper.database.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DataProcessingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM income";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet orderResults = stmt.executeQuery();
            
            System.out.println("Results of the orderresults");
            System.out.println(orderResults);
            
            request.setAttribute("orderResults", orderResults);
            request.setAttribute("metaData", orderResults.getMetaData());

            request.getRequestDispatcher("/submit-data.jsp").forward(request, response);
            
        } catch (SQLException e) {
            response.getWriter().write("Error" + e.getMessage());
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Received POST request at DataProcessingServlet");  


        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); 
        System.out.println("User ID in session: " + userId);

        if (userId == null) {
            System.out.println("User not logged in");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Gson gson = new Gson();
            Map<String, String> data = gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());


            String date = data.get("date");
            String description = data.get("description");
            String category = data.get("category");
            double income = Double.parseDouble(data.getOrDefault("income", "0"));
            double expense = Double.parseDouble(data.getOrDefault("expense", "0"));

            // Logging received data for debugging
            System.out.println("Received data:");
            System.out.println("Date: " + date);
            System.out.println("Description: " + description);
            System.out.println("Category: " + category);
            System.out.println("Income: " + income);
            System.out.println("Expense: " + expense);

            String query;
            if (income > 0) {
                query = "INSERT INTO income (user_id, source, amount, date, description) VALUES (?, ?, ?, ?, ?)";
            } else {
                query = "INSERT INTO expenses (user_id, category, amount, date, description) VALUES (?, ?, ?, ?, ?)";
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setString(2, category);
                stmt.setDouble(3, income > 0 ? income : expense);
                stmt.setDate(4, java.sql.Date.valueOf(date));
                stmt.setString(5, description);
                stmt.executeUpdate();
                System.out.println("Data successfully saved to database.");
            }


            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();  
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
