package com.example.ritmofit.reservas.service;

import com.example.ritmofit.home.model.Course;

import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de las inscripciones
 */
public interface EnrollmentService {
    
    /**
     * Inscribir al usuario en un curso
     * @param course el curso al que se inscribe
     * @return true si la inscripción fue exitosa, false si ya estaba inscrito
     */
    boolean enrollInCourse(Course course);
    
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
     * @return true si la cancelación fue exitosa
     */
    boolean unenrollFromCourse(String courseName);
}
