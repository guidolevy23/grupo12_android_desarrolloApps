package com.example.ritmofit.reservas.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ritmofit.home.model.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * Implementación del repositorio de inscripciones usando SharedPreferences
 * Almacena los cursos inscritos en formato JSON
 */
@Singleton
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    
    private static final String TAG = "EnrollmentRepository";
    private static final String PREFS_NAME = "enrollment_preferences";
    private static final String KEY_ENROLLED_COURSES = "enrolled_courses";
    
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    
    @Inject
    public EnrollmentRepositoryImpl(@ApplicationContext Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }
    
    @Override
    public void enrollInCourse(Course course) {
        try {
            List<Course> enrolledCourses = getEnrolledCourses();
            
            // Verificar si ya está inscrito (por nombre del curso)
            boolean alreadyEnrolled = enrolledCourses.stream()
                    .anyMatch(c -> c.getName().equals(course.getName()));
            
            if (!alreadyEnrolled) {
                enrolledCourses.add(course);
                saveEnrolledCourses(enrolledCourses);
                Log.d(TAG, "Usuario inscrito en curso: " + course.getName());
            } else {
                Log.d(TAG, "Usuario ya está inscrito en: " + course.getName());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al inscribir en curso", e);
        }
    }
    
    @Override
    public List<Course> getEnrolledCourses() {
        try {
            String coursesJson = sharedPreferences.getString(KEY_ENROLLED_COURSES, "");
            if (coursesJson.isEmpty()) {
                return new ArrayList<>();
            }
            
            Type listType = new TypeToken<List<Course>>(){}.getType();
            List<Course> courses = gson.fromJson(coursesJson, listType);
            return courses != null ? courses : new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener cursos inscritos", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean isEnrolledInCourse(String courseName) {
        List<Course> enrolledCourses = getEnrolledCourses();
        return enrolledCourses.stream()
                .anyMatch(course -> course.getName().equals(courseName));
    }
    
    @Override
    public void unenrollFromCourse(String courseName) {
        try {
            List<Course> enrolledCourses = getEnrolledCourses();
            enrolledCourses.removeIf(course -> course.getName().equals(courseName));
            saveEnrolledCourses(enrolledCourses);
            Log.d(TAG, "Usuario desinscrito del curso: " + courseName);
        } catch (Exception e) {
            Log.e(TAG, "Error al desinscribir del curso", e);
        }
    }
    
    @Override
    public void clearAllEnrollments() {
        try {
            sharedPreferences.edit().remove(KEY_ENROLLED_COURSES).apply();
            Log.d(TAG, "Todas las inscripciones han sido eliminadas");
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar inscripciones", e);
        }
    }
    
    /**
     * Guarda la lista de cursos inscritos en SharedPreferences
     * @param courses lista de cursos a guardar
     */
    private void saveEnrolledCourses(List<Course> courses) {
        try {
            String coursesJson = gson.toJson(courses);
            sharedPreferences.edit()
                    .putString(KEY_ENROLLED_COURSES, coursesJson)
                    .apply();
        } catch (Exception e) {
            Log.e(TAG, "Error al guardar cursos inscritos", e);
        }
    }
}
