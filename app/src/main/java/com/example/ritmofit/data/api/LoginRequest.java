package com.example.ritmofit.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// DTOs simples para login
class LoginRequest {
    String email;
    String otp;

    public LoginRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
}
