package com.example.ritmofit.data.repository;

import com.example.ritmofit.callback.CoursesCallback;

public interface CourseRepository {

    void getAllByName(String name, CoursesCallback callback);
}
