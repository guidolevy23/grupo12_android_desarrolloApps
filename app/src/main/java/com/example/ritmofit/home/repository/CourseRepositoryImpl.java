package com.example.ritmofit.home.repository;

import android.util.Log;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.data.api.model.CourseResponse;
import com.example.ritmofit.data.api.model.PageResponse;
import com.example.ritmofit.home.http.CoursesApi;
import com.example.ritmofit.home.model.Course;
import com.example.ritmofit.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CourseRepositoryImpl implements CourseRepository {

    private static final String TAG = "CourseRepositoryImpl";
    private final CoursesApi api;

    @Inject
    public CourseRepositoryImpl(CoursesApi api) {
        this.api = api;
    }

    // Mapper de API → modelo de dominio
    static Course toModel(CourseResponse response) {
        try {
            LocalDateTime start = LocalDateTime.parse(
                    response.getStartsAt(),
                    DateUtils.BACKEND_DATETIME_FORMATTER
            );

            LocalDateTime end = LocalDateTime.parse(
                    response.getEndsAt(),
                    DateUtils.BACKEND_DATETIME_FORMATTER
            );

            return new Course(
                    response.getName(),
                    response.getDescription(),
                    response.getProfessor(),
                    response.getBranch(),
                    start,
                    end
            );
        } catch (Exception e) {
            Log.e(TAG, "Error parsing course dates: " + e.getMessage());
            // Devolver curso con fechas por defecto o null según tu lógica
            return new Course(
                    response.getName(),
                    response.getDescription(),
                    response.getProfessor(),
                    response.getBranch(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1)
            );
        }
    }

    @Override
    public void getAllByName(String name, DomainCallback<List<Course>> callback) {
        searchCourses(name, null, null, null, null, callback);
    }

    @Override
    public void getAllByProfessor(String professor, DomainCallback<List<Course>> callback) {
        searchCourses(null, professor, null, null, null, callback);
    }

    @Override
    public void getAllByDateBetween(String start, String end, DomainCallback<List<Course>> callback) {
        try {
            LocalDate startDate = LocalDate.parse(start, DateUtils.API_DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(end, DateUtils.API_DATE_FORMATTER);

            String startIso = startDate.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String endIso = endDate.atTime(23, 59, 59).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            searchCourses(null, null, null, startIso, endIso, callback);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing dates: " + e.getMessage());
            callback.onError(e);
        }
    }

    @Override
    public void getAllByBranch(String branch, DomainCallback<List<Course>> callback) {
        searchCourses(null, null, branch, null, null, callback);
    }

    @Override
    public void searchCourses(
            String name,
            String professor,
            String branch,
            String startDate,
            String endDate,
            DomainCallback<List<Course>> callback
    ) {
        Log.d(TAG, "Searching courses with params: " +
                "name=" + name + ", professor=" + professor + ", branch=" + branch +
                ", startDate=" + startDate + ", endDate=" + endDate);

        Call<PageResponse<CourseResponse>> call = api.searchCourses(
                name, professor, branch, startDate, endDate, 0, 100
        );
        enqueueCall(call, callback, "Error en búsqueda");
    }

    private void enqueueCall(Call<PageResponse<CourseResponse>> call,
                             DomainCallback<List<Course>> callback,
                             String errorMessage) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<PageResponse<CourseResponse>> call,
                                   @NotNull Response<PageResponse<CourseResponse>> response) {

                Log.d(TAG, "Response received. Success: " + response.isSuccessful());

                if (!response.isSuccessful()) {
                    Log.e(TAG, "HTTP Error: " + response.code() + " - " + response.message());
                    callback.onError(new Exception("HTTP Error: " + response.code()));
                    return;
                }

                if (response.body() == null) {
                    Log.e(TAG, "Response body is null");
                    callback.onError(new Exception("Response body is null"));
                    return;
                }

                try {
                    PageResponse<CourseResponse> pageResponse = response.body();
                    List<CourseResponse> courseResponses = pageResponse.getContent();

                    Log.d(TAG, "Number of courses in response: " +
                            (courseResponses != null ? courseResponses.size() : "null"));

                    if (courseResponses == null || courseResponses.isEmpty()) {
                        Log.d(TAG, "No courses found");
                        callback.onSuccess(List.of());
                        return;
                    }

                    List<Course> courses = courseResponses.stream()
                            .map(CourseRepositoryImpl::toModel)
                            .collect(Collectors.toList());

                    Log.d(TAG, "Successfully mapped " + courses.size() + " courses");
                    callback.onSuccess(courses);

                } catch (Exception e) {
                    Log.e(TAG, "Error processing response: " + e.getMessage(), e);
                    callback.onError(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PageResponse<CourseResponse>> call,
                                  @NotNull Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                callback.onError(t);
            }
        });
    }
}