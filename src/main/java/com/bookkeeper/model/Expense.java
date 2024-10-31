/*
 * File name: Expense.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class represents expenses, holding details like amount, description, and date.
 */

package com.bookkeeper.model;

public class Expense {
    private String category;
    private double amount;
    private String date;
    private String description;

    public Expense(String category, double amount, String date, String description) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
