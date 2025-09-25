package com.example.ritmofit.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

    private String name;
    private String description;
    private String professor;
    private String branch;
    private String startsAt;
    private String endsAt;

    // --- Constructor completo ---
    public Course(String name, String description, String professor, String branch, String startsAt, String endsAt) {
        this.name = name;
        this.description = description;
        this.professor = professor;
        this.branch = branch;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    // --- Constructor para Parcel ---
    protected Course(Parcel in) {
        name = in.readString();
        description = in.readString();
        professor = in.readString();
        branch = in.readString();
        startsAt = in.readString();
        endsAt = in.readString();
    }

    // --- Parcelable ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(professor);
        dest.writeString(branch);
        dest.writeString(startsAt);
        dest.writeString(endsAt);
    }

    @Override
    public int describeContents() {
        return 0;
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

    // --- Getters ---
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfessor() {
        return professor;
    }

    public String getBranch() {
        return branch;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    // --- Setters (si querés que sea mutable) ---
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setStartsAt(String startsAt) { this.startsAt = startsAt; }
    public void setEndsAt(String endsAt) { this.endsAt = endsAt; }
}
