package com.example.ritmofit.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout; // Changed from RecyclerView
import android.widget.TextView;    // Assuming item layout has a TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ritmofit.R;
import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.home.service.CourseService;
import com.google.android.material.card.MaterialCardView; // For the item view

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    CourseService courseService;

    private LinearLayout coursesContainerLayout; // Changed from RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesContainerLayout = view.findViewById(R.id.coursesContainerLayout); // ID from fragment_home.xml
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
                        // CORRECCIÓN: Inflar item_course, no fragment_course_detail
                        View itemCardView = inflater.inflate(
                                R.layout.item_course, // ← Este es el layout correcto
                                coursesContainerLayout,
                                false
                        );

                        TextView nameTextView = itemCardView.findViewById(R.id.courseName);
                        TextView descTextView = itemCardView.findViewById(R.id.courseDescription);
                        TextView professorTextView = itemCardView.findViewById(R.id.courseProfessor);

                        nameTextView.setText(course.getName() != null ? course.getName() : "");
                        descTextView.setText(course.getDescription() != null ? course.getDescription() : "");
                        professorTextView.setText(course.getProfessor() != null ? course.getProfessor() : "");

                        itemCardView.setOnClickListener(v -> {
                            Bundle args = new Bundle();
                            args.putParcelable("courseId", course);
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
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                            "Error al cargar las clases: " + error.getMessage(),
                            Toast.LENGTH_LONG).show());
                }
            }
        });
    }
}