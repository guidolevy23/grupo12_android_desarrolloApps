package com.example.ritmofit.home.service;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.home.model.Course;

import java.util.List;

public interface CourseService {
    void getAllByName(String name, DomainCallback<List<Course>> callback);
    void getAllByProfessor(String professor, DomainCallback<List<Course>> callback);
    void getAllByDateBetween(String start, String end, DomainCallback<List<Course>> callback);
}
