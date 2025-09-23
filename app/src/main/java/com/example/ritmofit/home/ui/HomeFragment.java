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
        String searchTerm = "CrossFit"; // This should ideally come from user input
        courseService.getAllByName(searchTerm, new DomainCallback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                if (getActivity() == null) return;

                List<String> courseDisplayStrings = courses.stream()
                        .map(course -> String.join(" - ",
                                course.getName() != null ? course.getName() : "",
                                course.getDescription() != null ? course.getDescription() : "",
                                course.getProfessor() != null ? course.getProfessor() : ""))
                        .collect(Collectors.toList());

                requireActivity().runOnUiThread(() -> {
                    coursesContainerLayout.removeAllViews(); // Clear previous items

                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    for (String courseDisplayString : courseDisplayStrings) {
                        // Assume list_item_course_material.xml is your Material Design item layout
                        // This layout should contain a TextView, e.g., R.id.item_course_title
                        MaterialCardView itemCardView = (MaterialCardView) inflater.inflate(R.layout.fragment_course_detail, coursesContainerLayout, false);
                        
                        // Find the TextView within the card.
                        // You'll need to define R.id.item_course_title in list_item_course_material.xml
                        TextView courseTitleTextView = itemCardView.findViewById(R.id.course);
                        if (courseTitleTextView != null) {
                            courseTitleTextView.setText(courseDisplayString);
                        }

                        itemCardView.setOnClickListener(v -> {
                            Bundle args = new Bundle();
                            // We use the display string as the ID as per previous logic.
                            // Consider passing the actual Course ID if available and more appropriate.
                            args.putString("courseId", courseDisplayString);
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_detailFragment, args);
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
