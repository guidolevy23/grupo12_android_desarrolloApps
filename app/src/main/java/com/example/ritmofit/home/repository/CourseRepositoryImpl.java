package com.example.ritmofit.home.repository;

import android.util.Log;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.data.api.model.CourseResponse;
import com.example.ritmofit.data.api.model.CoursesResponse;
import com.example.ritmofit.data.api.model.PageResponse;
import com.example.ritmofit.home.http.CoursesApi;
import com.example.ritmofit.home.model.Course;

import org.jetbrains.annotations.NotNull;

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
                response.getProfessor(),
                response.getBranch(),
                response.getStartsAt(),
                response.getEndsAt()
        );
    }

    private final CoursesApi api;

    @Inject
    public CourseRepositoryImpl(CoursesApi api) {
        this.api = api;
    }

    @Override
    public void getAllByName(String name, DomainCallback<List<Course>> callback) {
        Call<PageResponse<CoursesResponse>> call = api.getAllBy(name);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NotNull Call<PageResponse<CoursesResponse>> call,
                                   @NotNull Response<PageResponse<CoursesResponse>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onError(new Exception("Error al buscar las clases"));
                    return;
                }

                // Log para debuggear el formato de las fechas
                if (!response.body().getData().getCourses().isEmpty()) {
                    CourseResponse firstCourse = response.body().getData().getCourses().get(0);
                    Log.d("CourseRepo", "StartsAt: " + firstCourse.getStartsAt());
                    Log.d("CourseRepo", "EndsAt: " + firstCourse.getEndsAt());
                }

                List<Course> courses = response.body()
                        .getData()
                        .getCourses()
                        .stream()
                        .map(CourseRepositoryImpl::toModel)
                        .collect(Collectors.toList());
                callback.onSuccess(courses);
            }

            @Override
            public void onFailure(@NotNull Call<PageResponse<CoursesResponse>> call,
                                  @NotNull Throwable t) {
                callback.onError(t);
            }
        });
    }
}