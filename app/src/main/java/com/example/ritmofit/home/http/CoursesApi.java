package com.example.ritmofit.home.http;

import com.example.ritmofit.data.api.model.CourseResponse;
import com.example.ritmofit.data.api.model.CoursesResponse;
import com.example.ritmofit.data.api.model.PageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoursesApi {
    @GET("api/courses/search/byProfessor")
    Call<PageResponse<CoursesResponse>> getAllByProfessor(@Query("professor") String professor);

    @GET("api/courses/search/byName")
    Call<PageResponse<CoursesResponse>> getAllBy(@Query("name") String name);

    @GET("api/courses/search/byDateBetween")
    Call<PageResponse<CoursesResponse>> getAllByDateBetween(
            @Query("start") String start,
            @Query("end") String end
    );

    // 👇 Nuevo método para filtrar entre fechas
    @GET("courses/search/byDateBetween")
    Call<PageResponse<CourseResponse>> getAllByDateBetween(
            @Query("start") String start,
            @Query("end") String end,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/courses/search/byBranch")
    Call<PageResponse<CoursesResponse>> getAllByBranch(@Query("branch") String branch);


}
