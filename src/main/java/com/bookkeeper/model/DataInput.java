/*
 * File name: DataInput.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class handles the input data and prepares it to be stored in the system.
 */


package com.bookkeeper.model;

import java.util.ArrayList;
import java.util.List;

public class DataInput {
    private List<Order> orders = new ArrayList<>();

    public void processOrderData(String customerName, double price, int quantity, int id) {
        Order order = new Order(customerName, price, quantity, id);
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
