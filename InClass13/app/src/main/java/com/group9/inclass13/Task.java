package com.group9.inclass13;

import java.util.Date;

public class Task {
    private long _id;
    private String text;
    private String priority;
    private Date date;

    public Task(String text, String priority) {
        this.text = text;
        this.priority = priority;
        this.date = new Date();
    }

    public Task() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Task{" +
                "_id=" + _id +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
