package com.example.ritmofit.auth.http;

import com.example.ritmofit.auth.http.api.LoginRequest;
import com.example.ritmofit.auth.http.api.LoginResponse;
import com.example.ritmofit.auth.http.api.RegisterRequest;
import com.example.ritmofit.auth.http.api.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
