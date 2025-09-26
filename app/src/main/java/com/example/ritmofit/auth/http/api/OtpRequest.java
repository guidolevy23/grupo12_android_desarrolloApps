package com.example.ritmofit.auth.http.api;

public record OtpRequest(String email, String otp) {

    public OtpRequest(String email) {
        this(email, null);
    }
}
