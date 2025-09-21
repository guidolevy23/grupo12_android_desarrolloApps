package com.example.ritmofit.data.repository.impl;

import androidx.annotation.NonNull;

import com.example.ritmofit.callback.CoursesCallback;
import com.example.ritmofit.data.api.RitmoFitApiService;
import com.example.ritmofit.data.api.model.CourseResponse;
import com.example.ritmofit.data.api.model.CoursesResponse;
import com.example.ritmofit.data.api.model.PageResponse;
import com.example.ritmofit.data.repository.CourseRepository;
import com.example.ritmofit.model.Course;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CourseRepositoryImpl implements CourseRepository {

    private static Course toModel(CourseResponse response) {
        return new Course(
                response.getName(),
                response.getDescription(),
                response.getProfessor()
        );
    }

    private final RitmoFitApiService api;

    @Inject
    public CourseRepositoryImpl(RitmoFitApiService api) {
        this.api = api;
    }

    @Override
    public void getAllByName(String name, final CoursesCallback callback) {
        callback.onSuccess(List.of());
//        Call<PageResponse<CoursesResponse>> call = api.getAllBy(name);
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<PageResponse<CoursesResponse>> call,
//                                   @NonNull Response<PageResponse<CoursesResponse>> response) {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.onError(new Exception("Error al buscar las clases"));
//                    return;
//                }
//                List<Course> courses = response.body()
//                        .getData()
//                        .getCourses()
//                        .stream()
//                        .map(CourseRepositoryImpl::toModel)
//                        .collect(Collectors.toList());
//                callback.onSuccess(courses);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<PageResponse<CoursesResponse>> call,
//                                  @NonNull Throwable t) {
//                callback.onError(t);
//            }
//        });
    }

//    Random random = new Random();
//    List<String> data = random.ints(10)
//            .mapToObj(Integer::toString)
//            .collect(Collectors.toList());
//    List<Course> courses = new ArrayList<>();
//        for (String i : data) {
//        courses.add(new Course(
//                i,
//                i,
//                i
//        ));
//    }
//        callback.onSuccess(courses);
}
