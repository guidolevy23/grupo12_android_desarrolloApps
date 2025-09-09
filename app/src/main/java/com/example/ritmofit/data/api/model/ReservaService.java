package com.example.ritmofit.data.api.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservaService {

    @POST("reservas")
    Call<ReservaResponse> crearReserva(@Body ReservaRequest request);

    @DELETE("reservas/{id}")
    Call<Void> cancelarReserva(@Path("id") Long id);

    @GET("reservas/mias")
    Call<List<ReservaResponse>> getMisReservas();
}
