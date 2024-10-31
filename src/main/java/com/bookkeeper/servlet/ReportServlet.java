package com.bookkeeper.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.bookkeeper.database.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String reportType = request.getParameter("reportType");
        
        if (request.getParameter("reportType") == null) {
        	response.sendRedirect("view-reports.html");
        }
        
        String date = request.getParameter("date");
        
        System.out.println("Report Type: " + reportType);
        System.out.println("Date Parameter: " + date);

        List<Map<String, Object>> reportData = new ArrayList<>();
        double totalIncome = 0;
        double totalExpense = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = buildQuery(reportType, request);
            System.out.println(query);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, date);
                if (reportType.equals("month")) {
                    stmt.setString(2, date);
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", rs.getDate("date"));
                    item.put("description", rs.getString("description"));
                    item.put("category", rs.getString("category"));
                    item.put("income", rs.getDouble("income"));
                    item.put("expense", rs.getDouble("expense"));

                    totalIncome += rs.getDouble("income");
                    totalExpense += rs.getDouble("expense");

                    reportData.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        double balance = totalIncome - totalExpense;


        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("reportData", reportData);
        jsonResponse.put("totals", Map.of("totalIncome", totalIncome, "totalExpense", totalExpense, "balance", balance));


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    private String buildQuery(String reportType, HttpServletRequest request) {
    	
        switch (reportType) {
            case "day":
                return "SELECT * FROM income UNION SELECT * FROM expenses WHERE DATE = " + request.getParameter("date");
            case "week":
                return "SELECT * FROM income UNION SELECT * FROM expenses WHERE YEARWEEK(date, 1) = YEARWEEK(?, 1)";
            case "month":
                return "SELECT * FROM income UNION SELECT * FROM expenses WHERE MONTH(date) = MONTH(?) AND YEAR(date) = YEAR(?)";
            default:
                throw new IllegalArgumentException("Invalid report type");
        }
    }
}
