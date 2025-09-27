package com.example.ritmofit.reservas.service;

import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.reservas.repository.EnrollmentRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementación del servicio de inscripciones
 * Contiene la lógica de negocio para manejar las inscripciones de cursos
 */
@Singleton
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    
    @Inject
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }
    
    @Override
    public boolean enrollInCourse(Course course) {
        if (course == null || course.getName() == null || course.getName().trim().isEmpty()) {
            return false;
        }
        
        // Verificar si ya está inscrito
        if (enrollmentRepository.isEnrolledInCourse(course.getName())) {
            return false; // Ya estaba inscrito
        }
        
        // Realizar la inscripción
        enrollmentRepository.enrollInCourse(course);
        return true;
    }
    
    @Override
    public List<Course> getEnrolledCourses() {
        return enrollmentRepository.getEnrolledCourses();
    }
    
    @Override
    public boolean isEnrolledInCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            return false;
        }
        return enrollmentRepository.isEnrolledInCourse(courseName);
    }
    
    @Override
    public boolean unenrollFromCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            return false;
        }
        
        if (!enrollmentRepository.isEnrolledInCourse(courseName)) {
            return false; // No estaba inscrito
        }
        
        enrollmentRepository.unenrollFromCourse(courseName);
        return true;
    }
}
