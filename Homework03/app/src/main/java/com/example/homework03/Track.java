package com.example.homework03;

import java.io.Serializable;

//Homework Assignment 03
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Track implements Serializable {
    String title, trackPrice, artist, date, genre, album, albumPrice, artworkURL;

    public Track() {
    }

    public Track(String title, String trackPrice, String artist, String date, String genre, String album, String albumPrice, String artworkURL) {
        this.title = title;
        this.trackPrice = trackPrice;
        this.artist = artist;
        this.date = date;
        this.genre = genre;
        this.album = album;
        this.albumPrice = albumPrice;
        this.artworkURL = artworkURL;
    }

    @Override
    public String toString() {
        return "Track{" +
                "title='" + title + '\'' +
                ", trackPrice='" + trackPrice + '\'' +
                ", artist='" + artist + '\'' +
                ", date='" + date + '\'' +
                ", genre='" + genre + '\'' +
                ", album='" + album + '\'' +
                ", albumPrice='" + albumPrice + '\'' +
                ", artworkURL='" + artworkURL + '\'' +
                '}';
    }
}