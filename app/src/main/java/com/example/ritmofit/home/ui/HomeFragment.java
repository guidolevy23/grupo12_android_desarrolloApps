package com.example.ritmofit.home.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ritmofit.R;
import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.home.service.CourseService;
import com.example.ritmofit.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    CourseService courseService;

    private LinearLayout coursesContainerLayout;
    private Button btnOpenFilters;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesContainerLayout = view.findViewById(R.id.coursesContainerLayout);
        btnOpenFilters = view.findViewById(R.id.btnOpenFilters);

        if (coursesContainerLayout == null) {
            Toast.makeText(getContext(), "Error: No se encontró el contenedor de cursos", Toast.LENGTH_LONG).show();
            return;
        }

        // Cargar todos los cursos al inicio
        loadAllCourses();

        // Configurar el botón de filtros
        btnOpenFilters.setOnClickListener(v -> openFiltersDialog());
    }

    private void loadAllCourses() {
        loadByName(""); // Cargar todos los cursos
    }

    private void openFiltersDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_filters, null);
        dialog.setContentView(sheetView);

        EditText inputName = sheetView.findViewById(R.id.inputName);
        EditText inputProfessor = sheetView.findViewById(R.id.inputProfessor);
        EditText inputBranch = sheetView.findViewById(R.id.inputBranch);
        EditText inputStart = sheetView.findViewById(R.id.inputStartDate);
        EditText inputEnd = sheetView.findViewById(R.id.inputEndDate);
        Button btnApply = sheetView.findViewById(R.id.btnApplyFilters);

        // DatePickers
        inputStart.setOnClickListener(x -> showDatePickerDialog(inputStart));
        inputEnd.setOnClickListener(x -> showDatePickerDialog(inputEnd));

        // Aplicar filtros
        btnApply.setOnClickListener(x -> {
            String name = inputName.getText().toString().trim();
            String professor = inputProfessor.getText().toString().trim();
            String branch = inputBranch.getText().toString().trim();
            String start = inputStart.getTag() != null ? inputStart.getTag().toString() : "";
            String end = inputEnd.getTag() != null ? inputEnd.getTag().toString() : "";

            applyFilters(name, professor, branch, start, end);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDatePickerDialog(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(requireContext(),
                (view, y, m, d) -> {
                    LocalDate selectedDate = LocalDate.of(y, m + 1, d);

                    // Mostrar formato lindo en el EditText
                    String displayDate = selectedDate.format(DateUtils.DISPLAY_LONG_DATE_FORMATTER);
                    target.setText(displayDate);

                    // Guardar formato ISO en el tag (para backend)
                    String apiDate = selectedDate.format(DateUtils.API_DATE_FORMATTER);
                    target.setTag(apiDate);
                }, year, month, day);

        datePicker.show();
    }

    private void applyFilters(String name, String professor, String branch, String start, String end) {
        if (!name.isEmpty()) {
            loadByName(name);
        } else if (!professor.isEmpty()) {
            loadByProfessor(professor);
        } else if (!branch.isEmpty()) {
            loadByBranch(branch);
        } else if (!start.isEmpty() && !end.isEmpty()) {
            loadByDate(start, end);
        } else {
            // Si no hay filtros → mostrar todos los cursos
            loadAllCourses();
        }
    }

    private void loadByName(String name) {
        courseService.getAllByName(name, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateCourseCards(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByProfessor(String professor) {
        courseService.getAllByProfessor(professor, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateCourseCards(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByBranch(String branch) {
        courseService.getAllByBranch(branch, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateCourseCards(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByDate(String start, String end) {
        courseService.getAllByDateBetween(start, end, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateCourseCards(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void updateCourseCards(List<Course> courses) {
        requireActivity().runOnUiThread(() -> {
            coursesContainerLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (courses.isEmpty()) {
                // Mostrar mensaje cuando no hay cursos
                TextView emptyView = new TextView(getContext());
                emptyView.setText("No se encontraron cursos");
                emptyView.setTextSize(16f);
                emptyView.setPadding(32, 32, 32, 32);
                emptyView.setGravity(android.view.Gravity.CENTER);
                coursesContainerLayout.addView(emptyView);
                return;
            }

            for (Course course : courses) {
                // Inflar la tarjeta del curso
                View itemCardView = inflater.inflate(
                        R.layout.item_course,
                        coursesContainerLayout,
                        false
                );

                TextView nameTextView = itemCardView.findViewById(R.id.courseName);
                TextView descTextView = itemCardView.findViewById(R.id.courseDescription);
                TextView professorTextView = itemCardView.findViewById(R.id.courseProfessor);

                if (nameTextView != null) {
                    nameTextView.setText(course.getName() != null ? course.getName() : "");
                }
                if (descTextView != null) {
                    descTextView.setText(course.getDescription() != null ? course.getDescription() : "");
                }
                if (professorTextView != null) {
                    professorTextView.setText(course.getProfessor() != null ? course.getProfessor() : "");
                }

                itemCardView.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putParcelable("course", course);
                    Navigation.findNavController(v).navigate(
                            R.id.action_homeFragment_to_detailFragment,
                            args
                    );
                });

                coursesContainerLayout.addView(itemCardView);
            }
        });
    }

    private void showError(Throwable error) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                "Error al cargar las clases: " + error.getMessage(),
                Toast.LENGTH_LONG).show());
    }
}