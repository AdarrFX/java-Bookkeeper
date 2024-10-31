/*
 * File name: Reporting.java
 * Author: Maya Erlich, Adam Pachulski, Nu Vo
 * Course: CST8288 – 450
 * Assignment: Capstone Project: Part II
 * Date: Nov 1, 2024
 * Professor: Jesus Federico
 * 
 * Purpose: This class creates different reports using the data collected in the system.
 */


package com.bookkeeper.model;

import java.util.List;

public class Reporting {

    public void generateIncomeReport(List<Income> incomes) {
        double totalIncome = incomes.stream()
                                    .mapToDouble(Income::getAmount)
                                    .sum();
        System.out.println("Total Income: $" + totalIncome);
    }

    public void generateExpenseReport(List<Expense> expenses) {
        double totalExpenses = expenses.stream()
                                       .mapToDouble(Expense::getAmount)
                                       .sum();
        System.out.println("Total Expenses: $" + totalExpenses);
    }

    public void generateProfitReport(List<Income> incomes, List<Expense> expenses) {
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double profit = totalIncome - totalExpenses;
        System.out.println("Total Profit: $" + profit);
    }
}
