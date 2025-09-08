package com.example.ritmofit.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HistorialService {

    @GET("historial")
    Call<List<HistorialResponse>> getHistorial(
            @Query("desde") String desde,
            @Query("hasta") String hasta
    );
}
