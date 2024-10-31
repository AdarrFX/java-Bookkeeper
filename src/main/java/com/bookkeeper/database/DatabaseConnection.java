/*
 * File name: DatabaseConnection.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class connects the application to the database, ensuring smooth 
 *          communication for storing and retrieving data.
 */

package com.bookkeeper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	    private static final String URL = "jdbc:postgresql://localhost:5433/lab03";
	    private static final String USER = "postgres";
	    private static final String PASSWORD = "eclipse_082015";

	    public static Connection getConnection() throws SQLException {
	        try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            throw new SQLException("PostgreSQL Driver not found!", e);
	        }
	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    }
	}