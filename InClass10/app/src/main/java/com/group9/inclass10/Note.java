package com.group9.inclass10;

import java.io.Serializable;
import java.util.Date;

//In-Class10
//Group 9
//Rockford Stoller

public class Note implements Serializable {
    String noteId, userId, noteText;
    int v;

    public Note() {
    }

    public Note(String noteId, String userId, String noteText, int v) {
        this.noteId = noteId;
        this.userId = userId;
        this.noteText = noteText;
        this.v = v;
    }
}
