package com.group9.inclass08;

import java.io.Serializable;

//In Class Assignment 08
//Group 9
//Rockford Stoller

public class Expense implements Serializable {
    String name, category, date;
    Double amount;

    public Expense() {
    }

    public Expense(String name, String category, String date, Double amount) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
