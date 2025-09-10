package com.example.ritmofit.data.api;

import com.example.ritmofit.data.api.model.CoursesResponse;
import com.example.ritmofit.data.api.model.PageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RitmoFitApiService {

    @GET("/courses/search/byName")
    Call<PageResponse<CoursesResponse>> getAllBy(@Query("name") String name);
}
