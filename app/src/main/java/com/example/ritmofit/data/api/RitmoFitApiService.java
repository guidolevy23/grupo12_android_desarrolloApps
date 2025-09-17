package com.example.ritmofit.data.api;

import com.example.ritmofit.data.api.model.auth.*;
import com.example.ritmofit.data.api.model.usuario.*;
import com.example.ritmofit.data.api.model.reserva.*;
import com.example.ritmofit.data.api.model.historial.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RitmoFitApiService {

    // ---------- AUTH ----------
    @POST("/api/auth/request-otp")
    Call<AuthResponseDTO> requestOtp(@Body OtpRequestDTO request);

    @POST("/api/auth/verify-otp")
    Call<AuthResponseDTO> verifyOtp(@Body OtpVerifyRequestDTO request);


    // ---------- USUARIOS ----------
    @GET("/api/usuarios/{id}")
    Call<PerfilResponseDTO> getUsuario(@Path("id") Long id);

    @PUT("/api/usuarios/{id}")
    Call<PerfilResponseDTO> updateUsuario(
            @Path("id") Long id,
            @Body PerfilUpdateRequestDTO updateRequest
    );


    // ---------- RESERVAS ----------
    @POST("/api/reservas")
    Call<ReservaResponseDTO> crearReserva(@Body ReservaCreateRequestDTO request);

    @GET("/api/reservas/usuario/{usuarioId}")
    Call<List<ReservaResponseDTO>> listarReservasUsuario(@Path("usuarioId") Long usuarioId);

    @DELETE("/api/reservas/{id}")
    Call<Void> cancelarReserva(@Path("id") Long id);


    // ---------- HISTORIAL ----------
    @POST("/api/historial/filtrar")
    Call<List<AsistenciaResponseDTO>> filtrarHistorial(@Body HistorialFilterRequestDTO request);

    @GET("/api/historial/{usuarioId}")
    Call<List<AsistenciaResponseDTO>> obtenerHistorial(@Path("usuarioId") Long usuarioId);
}
