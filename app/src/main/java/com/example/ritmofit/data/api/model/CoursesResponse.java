package com.example.ritmofit.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CoursesResponse {

    @SerializedName("courses")
    private List<CourseResponse> courses;

    public List<CourseResponse> getCourses() {
        return courses;
    }
}