package com.example.ritmofit.home.service;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.home.repository.CourseRepository;

import java.util.List;

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
    public void getAllByName(String name, DomainCallback<List<Course>> callback) {
        repository.getAllByName(name, callback);
    }
}
