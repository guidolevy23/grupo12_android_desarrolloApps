package com.example.ritmofit.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

    private String name;
    private String description;
    private String professor;

    // --- Constructor ---
    public Course(String name, String description, String professor) {
        this.name = name;
        this.description = description;
        this.professor = professor;
    }

    // --- Constructor for Parcel ---
    protected Course(Parcel in) {
        name = in.readString();
        description = in.readString();
        professor = in.readString();
    }

    // --- Parcelable required methods ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(professor);
    }

    @Override
    public int describeContents() {
        return 0; // usually 0, unless file descriptors are used
    }

    // --- Getters (optional) ---
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfessor() {
        return professor;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
