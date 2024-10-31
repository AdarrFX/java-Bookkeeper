/*
 * File name: Order.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class defines orders, keeping track of what is purchased and its details.
 */

package com.bookkeeper.model;

public class Order {
    private String customerName;
    private double unitPrice;
    private int quantitySold;
    private int uniqueID; 
    private double totalSalePrice;

    // Constructor
    public Order(String customerName, double unitPrice, int quantitySold, int uniqueID) {
        this.customerName = customerName;
        this.unitPrice = unitPrice;
        this.quantitySold = quantitySold;
        this.uniqueID = uniqueID;
        this.totalSalePrice = calculateTotalSalePrice();
    }

    // Private method to calculate the total sale price
    private double calculateTotalSalePrice() {
        return unitPrice * quantitySold;
    }

    // Getter for customer name
    public String getCustomerName() {
        return customerName;
    }

    // Getter for total sale price
    public double getTotalSalePrice() {
        return totalSalePrice;
    }

    // Getter for uniqueID
    public int getUniqueID() {
        return uniqueID;
    }


    @Override
    public String toString() {
        return "Order ID: " + uniqueID +
               ", Customer: " + customerName +
               ", Quantity: " + quantitySold +
               ", Total: $" + totalSalePrice;
    }
}
