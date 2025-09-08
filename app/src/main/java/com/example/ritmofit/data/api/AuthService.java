package com.example.ritmofit.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/verify")   // <-- endpoint de tu backend
    Call<LoginResponse> login(@Body LoginRequest request);
}