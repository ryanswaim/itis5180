package com.example.inclass07;

import java.io.Serializable;

//In Class Assignment 07
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Expense implements Serializable {
    String name, category, amount, date;

    public Expense() {
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
