package com.example.ritmofit.callback;

import com.example.ritmofit.model.Course;

import java.util.List;

public interface CoursesCallback {
    void onSuccess(List<Course> courses);
    void onError(Throwable error);
}
