package com.example.ritmofit.data.api.model;

import com.example.ritmofit.data.api.model.usuario.PerfilResponseDTO;
import com.example.ritmofit.data.api.model.usuario.PerfilUpdateRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/{id}")
    Call<PerfilResponseDTO> getUser(@Path("id") Long id);

    @PUT("user/{id}")
    Call<PerfilResponseDTO> updateUser(@Path("id") Long id, @Body PerfilUpdateRequestDTO request);
}
