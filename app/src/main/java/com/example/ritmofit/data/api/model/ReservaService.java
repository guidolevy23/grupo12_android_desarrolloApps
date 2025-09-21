package com.example.ritmofit.data.api.model;

import com.example.ritmofit.data.api.model.reserva.ReservaCreateRequestDTO;
import com.example.ritmofit.data.api.model.reserva.ReservaResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservaService {

    @POST("reservas")
    Call<ReservaResponseDTO> crearReserva(@Body ReservaCreateRequestDTO request);

    @DELETE("reservas/{id}")
    Call<Void> cancelarReserva(@Path("id") Long id);

    @GET("reservas/mias")
    Call<List<ReservaResponseDTO>> getMisReservas();
}
