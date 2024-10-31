/*
 * File name: Income.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class models income entries with relevant details like amount and description.
 */

package com.bookkeeper.model;


public class Income {
    private String source;
    private double amount;
    private String date;
    private String description;

    public Income(String source, double amount, String date, String description) {
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getSource() { return source; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
