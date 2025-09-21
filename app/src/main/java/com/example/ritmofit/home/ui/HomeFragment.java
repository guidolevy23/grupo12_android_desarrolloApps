package com.example.ritmofit.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private List<String> coursesToDisplay;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        coursesToDisplay = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1,
                coursesToDisplay);
        listView.setAdapter(adapter);
        loadCourses();
        listView.setOnItemClickListener((parent, v, position, id) -> {
            String course = coursesToDisplay.get(position);
            Bundle args = new Bundle();
            args.putString("courseId", course);
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_detailFragment, args);
        });
    }

    private void loadCourses() {
        String searchTerm = "CrossFit"; // se deberia pedir en un form esto
        courseService.getAllByName(searchTerm, new DomainCallback<>() {
            @Override
            public void onSuccess(List<Course> courses) {
                coursesToDisplay.clear();
                coursesToDisplay.addAll(
                        courses.stream()
                                .map(course -> String.join(" - ",
                                        course.getName(),
                                        course.getDescription(),
                                        course.getProfessor()))
                                .collect(Collectors.toList())
                );
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }

            @Override
            public void onError(Throwable error) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                        "Error al cargar las clases: " + error.getMessage(),
                        Toast.LENGTH_LONG).show());
            }
        });
    }
}
