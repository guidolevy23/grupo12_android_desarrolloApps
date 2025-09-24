package com.example.ritmofit.home.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    CourseService courseService;

    private LinearLayout coursesContainerLayout;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesContainerLayout = view.findViewById(R.id.coursesContainerLayout);
        if (coursesContainerLayout == null) {
            Log.e(TAG, "coursesContainerLayout is null!");
            return;
        }
        loadCourses();
    }

    private void loadCourses() {
        String searchTerm = "CrossFit";
        courseService.getAllByName(searchTerm, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                if (getActivity() == null) return;

                requireActivity().runOnUiThread(() -> {
                    coursesContainerLayout.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    for (Course course : courses) {
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
                            Log.d(TAG, "Course clicked: " + course.getName());
                            Bundle args = new Bundle();
                            args.putParcelable("course", course); // Clave corregida
                            Navigation.findNavController(v).navigate(
                                    R.id.action_homeFragment_to_detailFragment,
                                    args
                            );
                        });

                        coursesContainerLayout.addView(itemCardView);
                    }
                });
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, "Error loading courses: " + error.getMessage());
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                            "Error al cargar las clases: " + error.getMessage(),
                            Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}