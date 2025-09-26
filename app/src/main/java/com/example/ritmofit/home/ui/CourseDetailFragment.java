package com.example.ritmofit.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ritmofit.R;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.reservas.service.EnrollmentService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseDetailFragment extends Fragment {

    private Course course;
    private TextView courseName, courseProfessor, courseDescription;
    private TextView courseSchedule, courseDifficulty, courseLocation, courseRequirements;
    private Chip courseCategory;
    private MaterialButton btnEnroll, btnShare;
    
    @Inject
    EnrollmentService enrollmentService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = getArguments().getParcelable("course");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        if (course != null) {
            displayCourseData();
            updateEnrollButtonState();
        } else {
            Toast.makeText(getContext(), "Error: No se encontró información del curso", Toast.LENGTH_LONG).show();
        }

        setupButtonListeners();
    }

    private void initializeViews(View view) {
        courseName = view.findViewById(R.id.courseName);
        courseProfessor = view.findViewById(R.id.courseProfessor);
        courseDescription = view.findViewById(R.id.courseDescription);

        // Campos de información del curso
        courseSchedule = view.findViewById(R.id.courseSchedule);
        courseDifficulty = view.findViewById(R.id.courseDifficulty);
        courseLocation = view.findViewById(R.id.courseLocation);
        courseRequirements = view.findViewById(R.id.courseRequirements);
        courseCategory = view.findViewById(R.id.courseCategory);

        btnEnroll = view.findViewById(R.id.btnEnroll);
        btnShare = view.findViewById(R.id.btnShare);
    }

    private void displayCourseData() {
        courseName.setText(course.getName());
        courseProfessor.setText("Profesor: " + course.getProfessor());
        courseDescription.setText(course.getDescription());

        // 🔹 usar el metodo getSchedule para mostrar el horario
        courseSchedule.setText(course.getSchedule());

        // Usar los métodos que devuelven valores basados en el nombre del curso
        courseDifficulty.setText(course.getDifficulty());
        courseLocation.setText(course.getBranch()); // Usamos branch como ubicación

        // Configurar el chip de categoría
        courseCategory.setText(course.getCategory());

        // Mantener requisitos estáticos por ahora
        courseRequirements.setText("• Ropa deportiva adecuada\n• Botella de agua\n• Toalla personal");
    }

    private void setupButtonListeners() {
        btnEnroll.setOnClickListener(v -> {
            if (course != null) {
                enrollInCourse(course.getName());
            }
        });

        btnShare.setOnClickListener(v -> {
            if (course != null) {
                shareCourse(course.getName());
            }
        });
    }

    private void enrollInCourse(String courseName) {
        if (course != null) {
            boolean success = enrollmentService.enrollInCourse(course);
            if (success) {
                Toast.makeText(getContext(), "¡Te inscribiste exitosamente en: " + courseName + "!", Toast.LENGTH_LONG).show();
                // Actualizar el botón para mostrar que ya está inscrito
                updateEnrollButtonState();
            } else {
                Toast.makeText(getContext(), "Ya estás inscrito en: " + courseName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareCourse(String courseName) {
        Toast.makeText(getContext(), "Compartiendo: " + courseName, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Actualiza el estado del botón de inscripción según si el usuario ya está inscrito
     */
    private void updateEnrollButtonState() {
        if (course != null && enrollmentService != null) {
            boolean isEnrolled = enrollmentService.isEnrolledInCourse(course.getName());
            if (isEnrolled) {
                btnEnroll.setText("Ya inscrito");
                btnEnroll.setEnabled(false);
            } else {
                btnEnroll.setText("Inscribirse");
                btnEnroll.setEnabled(true);
            }
        }
    }
}