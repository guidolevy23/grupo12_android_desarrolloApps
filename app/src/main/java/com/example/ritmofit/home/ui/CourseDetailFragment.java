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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

public class CourseDetailFragment extends Fragment {

    private Course course;
    private TextView courseName, courseProfessor, courseDescription;
    private TextView courseDuration, courseDifficulty, courseSchedule, courseLocation, courseRequirements;
    private Chip courseCategory;
    private MaterialButton btnEnroll, btnShare;

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

        // Inicializar vistas
        initializeViews(view);

        // Mostrar datos del curso
        if (course != null) {
            displayCourseData();
        } else {
            Toast.makeText(getContext(), "Error: No se encontró información del curso", Toast.LENGTH_LONG).show();
        }

        setupButtonListeners();
    }

    private void initializeViews(View view) {
        courseName = view.findViewById(R.id.courseName);
        courseProfessor = view.findViewById(R.id.courseProfessor);
        courseDescription = view.findViewById(R.id.courseDescription);
        courseDuration = view.findViewById(R.id.courseDuration);
        courseDifficulty = view.findViewById(R.id.courseDifficulty);
        courseSchedule = view.findViewById(R.id.courseSchedule);
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

        // Si tienes más campos en el modelo, agrégalos aquí
        // courseDuration.setText(course.getDuration());
        // courseDifficulty.setText(course.getDifficulty());
        // courseSchedule.setText(course.getSchedule());
        // courseLocation.setText(course.getLocation());
        // courseRequirements.setText(course.getRequirements());
        // courseCategory.setText(course.getCategory());
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
        Toast.makeText(getContext(), "Inscrito en: " + courseName, Toast.LENGTH_SHORT).show();
        // Lógica de inscripción
    }

    private void shareCourse(String courseName) {
        Toast.makeText(getContext(), "Compartiendo: " + courseName, Toast.LENGTH_SHORT).show();
        // Lógica para compartir
    }
}