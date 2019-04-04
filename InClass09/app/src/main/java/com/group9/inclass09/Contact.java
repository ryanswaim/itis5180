package com.group9.inclass09;

import java.io.Serializable;

//InClass09
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Contact implements Serializable {
    String name, email, phoneNumber, imageUrl;

    public Contact() {
    }

    public Contact(String name, String email, String phoneNumber, String imageUrl) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }
}
