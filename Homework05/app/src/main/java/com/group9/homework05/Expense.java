package com.group9.homework05;

import java.io.Serializable;

//Homework05
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Expense implements Serializable {
    String name, date, receiptUrl;
    int month, day, year;
    Double amount;

    public Expense() {
    }

    public Expense(String name, Double amount, String date, String receiptUrl, int month, int day, int year) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.receiptUrl = receiptUrl;
        this.month = month;
        this.day = day;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", receiptUrl='" + receiptUrl + '\'' +
                '}';
    }
}
