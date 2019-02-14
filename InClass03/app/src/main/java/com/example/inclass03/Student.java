package com.example.inclass03;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    //attributes
    int imageId;
    String fname;
    String lname;
    String stuId;
    String depart;

    //constructor
    public Student(int imageId, String fname, String lname, String stuId, String depart) {
        this.imageId = imageId;
        this.fname = fname;
        this.lname = lname;
        this.stuId = stuId;
        this.depart = depart;
    }

    //to string
    @Override
    public String toString() {
        return "Student{" +
                "imageId=" + imageId +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", stuId='" + stuId + '\'' +
                ", depart='" + depart + '\'' +
                '}';
    }

    //reading from parcel
    protected Student(Parcel in) {
        this.imageId = in.readInt();
        this.fname = in.readString();
        this.lname = in.readString();
        this.stuId = in.readString();
        this.depart = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {

            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //writing to parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(stuId);
        dest.writeString(depart);
    }
}
