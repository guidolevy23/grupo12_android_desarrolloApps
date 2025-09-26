package com.example.ritmofit.reservas.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ritmofit.R;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.reservas.service.EnrollmentService;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment para mostrar las reservas (cursos en los que el usuario se ha inscrito)
 * Separado del historial de asistencias para mejor organización
 */
@AndroidEntryPoint
public class ReservasFragment extends Fragment {
    
    @Inject
    EnrollmentService enrollmentService;
    
    private LinearLayout coursesContainer;
    private TextView emptyStateText;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservas, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        loadEnrolledCourses();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Recargar las inscripciones cuando se vuelve al fragment
        loadEnrolledCourses();
    }
    
    /**
     * Inicializa las vistas del fragment
     */
    private void initializeViews(View view) {
        coursesContainer = view.findViewById(R.id.coursesContainer);
        emptyStateText = view.findViewById(R.id.emptyStateText);
    }
    
    /**
     * Carga y muestra los cursos en los que el usuario está inscrito
     */
    private void loadEnrolledCourses() {
        List<Course> enrolledCourses = enrollmentService.getEnrolledCourses();
        
        if (enrolledCourses.isEmpty()) {
            showEmptyState();
        } else {
            showEnrolledCourses(enrolledCourses);
        }
    }
    
    /**
     * Muestra el estado vacío cuando no hay cursos inscritos
     */
    private void showEmptyState() {
        coursesContainer.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
    }
    
    /**
     * Muestra la lista de cursos inscritos
     */
    private void showEnrolledCourses(List<Course> courses) {
        emptyStateText.setVisibility(View.GONE);
        coursesContainer.setVisibility(View.VISIBLE);
        
        // Limpiar el contenedor
        coursesContainer.removeAllViews();
        
        // Agregar cada curso inscrito
        for (Course course : courses) {
            View courseCard = createCourseCard(course);
            coursesContainer.addView(courseCard);
        }
    }
    
    /**
     * Crea una tarjeta visual para mostrar un curso inscrito
     */
    private View createCourseCard(Course course) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.item_enrolled_course, coursesContainer, false);
        
        // Configurar los datos del curso
        TextView courseName = cardView.findViewById(R.id.courseName);
        TextView courseProfessor = cardView.findViewById(R.id.courseProfessor);
        TextView courseSchedule = cardView.findViewById(R.id.courseSchedule);
        TextView courseBranch = cardView.findViewById(R.id.courseBranch);
        
        courseName.setText(course.getName());
        courseProfessor.setText("Profesor: " + course.getProfessor());
        courseSchedule.setText("Horario: " + course.getSchedule());
        courseBranch.setText("Sede: " + course.getBranch());
        
        // Configurar el botón de cancelar inscripción
        View btnCancelEnrollment = cardView.findViewById(R.id.btnCancelEnrollment);
        btnCancelEnrollment.setOnClickListener(v -> cancelEnrollment(course));
        
        return cardView;
    }
    
    /**
     * Cancela la inscripción a un curso
     */
    private void cancelEnrollment(Course course) {
        boolean success = enrollmentService.unenrollFromCourse(course.getName());
        if (success) {
            Toast.makeText(getContext(), "Inscripción cancelada: " + course.getName(), Toast.LENGTH_SHORT).show();
            loadEnrolledCourses(); // Recargar la lista
        } else {
            Toast.makeText(getContext(), "Error al cancelar inscripción", Toast.LENGTH_SHORT).show();
        }
    }
}
