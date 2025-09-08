package com.example.ritmofit.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClaseService {

    @GET("clases")
    Call<List<ClaseResponse>> getClases(
            @Query("sede") String sede,
            @Query("disciplina") String disciplina,
            @Query("fecha") String fecha
    );
}
