package com.example.ritmofit.reservas.repository;

import com.example.ritmofit.home.model.Course;

import java.util.List;

/**
 * Repositorio para manejar las inscripciones a cursos
 * Permite almacenar y recuperar los cursos en los que el usuario se ha inscrito
 */
public interface EnrollmentRepository {
    
    /**
     * Inscribir al usuario en un curso
     * @param course el curso al que se inscribe
     */
    void enrollInCourse(Course course);
    
    /**
     * Obtener todos los cursos en los que el usuario está inscrito
     * @return lista de cursos inscritos
     */
    List<Course> getEnrolledCourses();
    
    /**
     * Verificar si el usuario está inscrito en un curso específico
     * @param courseName nombre del curso a verificar
     * @return true si está inscrito, false en caso contrario
     */
    boolean isEnrolledInCourse(String courseName);
    
    /**
     * Cancelar inscripción a un curso
     * @param courseName nombre del curso para cancelar inscripción
     */
    void unenrollFromCourse(String courseName);
    
    /**
     * Limpiar todas las inscripciones
     */
    void clearAllEnrollments();
}
