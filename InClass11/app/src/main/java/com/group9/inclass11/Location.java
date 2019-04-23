package com.group9.inclass11;

import java.util.ArrayList;

//In Class Assignment 11
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Location {
    ArrayList<LocPoint> points;
    String title;

    public Location(ArrayList<LocPoint> points, String title) {
        this.points = points;
        this.title = title;
    }

    public ArrayList<LocPoint> getPoints() {
        return points;
    }
}
