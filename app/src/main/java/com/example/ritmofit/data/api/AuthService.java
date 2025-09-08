package com.example.ritmofit.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    // Paso 1: pedir OTP por mail
    @POST("auth/login")
    Call<Void> requestOtp(@Body LoginRequest request);

    // Paso 2: verificar OTP y loguear
    @POST("auth/verify")
    Call<LoginResponse> verifyOtp(@Body LoginRequest request);
}

