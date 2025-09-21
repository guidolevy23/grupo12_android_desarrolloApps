package com.example.ritmofit.auth.http;

import com.example.ritmofit.auth.model.LoginRequest;
import com.example.ritmofit.auth.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
