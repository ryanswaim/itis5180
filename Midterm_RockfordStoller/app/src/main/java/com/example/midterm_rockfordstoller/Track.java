package com.example.midterm_rockfordstoller;

import java.io.Serializable;

public class Track implements Serializable {
    String trackName, albumName, artistName, updateTime, trackShareURL;

    public Track() {
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackName='" + trackName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", trackShareURL='" + trackShareURL + '\'' +
                '}';
    }
}