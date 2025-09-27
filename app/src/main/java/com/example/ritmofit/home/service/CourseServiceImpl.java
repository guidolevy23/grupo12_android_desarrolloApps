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
    public void searchCourses(
            String name,
            String professor,
            String branch,
            String startDate,
            String endDate,
            DomainCallback<List<Course>> callback
    ) {
        repository.searchCourses(name, professor, branch, startDate, endDate, callback);
    }

    @Override
    public void getAllByName(String name, DomainCallback<List<Course>> callback) {
        repository.getAllByName(name, callback);
    }

    @Override
    public void getAllByBranch(String branch, DomainCallback<List<Course>> callback) {
        repository.getAllByBranch(branch, callback);
    }

    @Override
    public void getAllByProfessor(String professor, DomainCallback<List<Course>> callback) {
        repository.getAllByProfessor(professor, callback);
    }

    @Override
    public void getAllByDateBetween(String start, String end, DomainCallback<List<Course>> callback) {
        repository.getAllByDateBetween(start, end, callback);
    }
}