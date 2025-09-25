package com.example.ritmofit.home.ui;

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

import java.util.ArrayList;
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

    private EditText inputName, inputProfessor, inputStart, inputEnd, inputBranch;
    private Button btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        inputName = view.findViewById(R.id.inputName);
        inputProfessor = view.findViewById(R.id.inputProfessor);
        inputStart = view.findViewById(R.id.inputStartDate);
        inputEnd = view.findViewById(R.id.inputEndDate);
        inputBranch = view.findViewById(R.id.inputBranch);
        btnFilter = view.findViewById(R.id.btnFilter);

        coursesToDisplay = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1,
                coursesToDisplay);
        listView.setAdapter(adapter);

        // Cargar todos los cursos al inicio
        loadAllCourses();

        btnFilter.setOnClickListener(v -> applyFilters());

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

    private void applyFilters() {
        String name = inputName.getText().toString().trim();
        String professor = inputProfessor.getText().toString().trim();
        String start = inputStart.getText().toString().trim();
        String end = inputEnd.getText().toString().trim();
        String branch = inputBranch.getText().toString().trim();

        if (!name.isEmpty()) {
            loadByName(name);
        } else if (!professor.isEmpty()) {
            loadByProfessor(professor);
        } else if (!branch.isEmpty()) {
            loadByBranch(branch);
        } else if (!start.isEmpty() && !end.isEmpty()) {
            loadByDate(start, end);
        } else {
            Toast.makeText(requireContext(), "Ingrese al menos un filtro", Toast.LENGTH_SHORT).show();
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
                                course.getStartsAt()))
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
