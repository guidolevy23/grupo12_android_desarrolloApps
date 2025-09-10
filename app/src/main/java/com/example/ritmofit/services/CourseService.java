package com.example.ritmofit.services;

import com.example.ritmofit.callback.CoursesCallback;

public interface CourseService {

    void getAllByName(String name, CoursesCallback callback);

}
