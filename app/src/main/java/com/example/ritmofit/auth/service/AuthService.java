package com.example.ritmofit.auth.service;

import com.example.ritmofit.auth.model.AuthRequest;
import com.example.ritmofit.auth.model.AuthResponse;
import com.example.ritmofit.core.Callback;

public interface AuthService {

    void register(AuthRequest request, Callback<AuthResponse> callback);
    void login(AuthRequest request, Callback<AuthResponse> callback);

}
