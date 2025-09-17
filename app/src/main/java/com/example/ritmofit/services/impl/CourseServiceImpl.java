package com.example.ritmofit.services.impl;

import com.example.ritmofit.callback.CoursesCallback;
import com.example.ritmofit.data.repository.CourseRepository;
import com.example.ritmofit.services.CourseService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    @Inject
    public CourseServiceImpl(CourseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getAllByName(String name, CoursesCallback callback) {
        repository.getAllByName(name, callback);
    }
}
