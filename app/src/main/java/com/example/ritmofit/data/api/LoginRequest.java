package com.example.ritmofit.data.api;

public class LoginRequest {
    public String email;
    public String otp; // puede ir vacío cuando solo se pide el OTP

    public LoginRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
}
