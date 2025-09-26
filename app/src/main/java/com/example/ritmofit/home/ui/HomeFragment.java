package com.example.ritmofit.home.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    CourseService courseService;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> coursesToDisplay;
    private Button btnOpenFilters;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        btnOpenFilters = view.findViewById(R.id.btnOpenFilters);

        coursesToDisplay = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1,
                coursesToDisplay);
        listView.setAdapter(adapter);

        // Cargar todos los cursos al inicio
        loadAllCourses();

        // Abrir filtros al presionar el botón
        btnOpenFilters.setOnClickListener(v -> openFiltersDialog(view));

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String course = coursesToDisplay.get(position);
            Bundle args = new Bundle();
            args.putString("courseId", course);
            Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_detailFragment, args);
        });
    }

    private void loadAllCourses() {
        courseService.getAllByName("", new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateList(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void openFiltersDialog(View parentView) {
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
            // ✅ Si no hay filtros → muestro todos los cursos
            loadAllCourses();
        }
    }


    private void loadByName(String name) {
        courseService.getAllByName(name, new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateList(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByProfessor(String professor) {
        courseService.getAllByProfessor(professor, new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateList(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByBranch(String branch) {
        courseService.getAllByBranch(branch, new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateList(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void loadByDate(String start, String end) {
        courseService.getAllByDateBetween(start, end, new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                updateList(courses);
            }

            @Override
            public void onError(Throwable error) {
                showError(error);
            }
        });
    }

    private void updateList(List<Course> courses) {
        coursesToDisplay.clear();
        coursesToDisplay.addAll(
                courses.stream()
                        .map(course -> String.format("%s - %s - %s - %s",
                                course.getName(),
                                course.getProfessor(),
                                course.getBranch(),
                                course.getStartsAt().format(DateUtils.DISPLAY_FULL_DATETIME_FORMATTER)
                        ))
                        .collect(Collectors.toList())
        );
        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    private void showError(Throwable error) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                "Error al cargar las clases: " + error.getMessage(),
                Toast.LENGTH_LONG).show());
    }
}
