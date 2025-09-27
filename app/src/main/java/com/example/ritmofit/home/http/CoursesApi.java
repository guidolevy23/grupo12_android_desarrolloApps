package com.example.ritmofit.home.http;

import com.example.ritmofit.data.api.model.CourseResponse;
import com.example.ritmofit.data.api.model.PageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoursesApi {
    @GET("api/courses/search")
    Call<PageResponse<CourseResponse>> searchCourses(
            @Query("name") String name,
            @Query("professor") String professor,
            @Query("branch") String branch,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") int page,
            @Query("size") int size
    );
}