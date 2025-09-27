package com.example.ritmofit.home.repository;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.home.model.Course;

import java.util.List;

public interface CourseRepository {
    void searchCourses(
            String name,
            String professor,
            String branch,
            String startDate,
            String endDate,
            DomainCallback<List<Course>> callback
    );

    // 🔹 Mantener métodos específicos para compatibilidad
    void getAllByName(String name, DomainCallback<List<Course>> callback);
    void getAllByProfessor(String professor, DomainCallback<List<Course>> callback);
    void getAllByDateBetween(String start, String end, DomainCallback<List<Course>> callback);
    void getAllByBranch(String branch, DomainCallback<List<Course>> callback);
}