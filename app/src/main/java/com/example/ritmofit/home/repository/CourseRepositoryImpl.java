package com.example.ritmofit.home.repository;

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

    private final CoursesApi api;

    @Inject
    public CourseRepositoryImpl(CoursesApi api) {
        this.api = api;
    }

    // Mapper de API → modelo de dominio
    private static Course toModel(CourseResponse response) {
        return new Course(
                response.getName(),
                response.getDescription(),
                response.getProfessor()
        );
    }

    @Override
    public void getAllByName(String name, DomainCallback<List<Course>> callback) {
        Call<PageResponse<CoursesResponse>> call = api.getAllBy(name);
        enqueueCall(call, callback, "Error al buscar por nombre");
    }

    @Override
    public void getAllByProfessor(String professor, DomainCallback<List<Course>> callback) {
        Call<PageResponse<CoursesResponse>> call = api.getAllByProfessor(professor);
        enqueueCall(call, callback, "Error al buscar por profesor");
    }

    @Override
    public void getAllByDateBetween(String start, String end, DomainCallback<List<Course>> callback) {
        Call<PageResponse<CoursesResponse>> call = api.getAllByDateBetween(start, end);
        enqueueCall(call, callback, "Error al buscar por fecha");
    }

    // 🔹 Método común para reducir código repetido
    private void enqueueCall(Call<PageResponse<CoursesResponse>> call,
                             DomainCallback<List<Course>> callback,
                             String errorMessage) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<PageResponse<CoursesResponse>> call,
                                   @NotNull Response<PageResponse<CoursesResponse>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onError(new Exception(errorMessage));
                    return;
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
