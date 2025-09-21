package com.example.ritmofit.auth.service;

import com.example.ritmofit.auth.model.AuthRequest;
import com.example.ritmofit.auth.model.AuthResponse;
import com.example.ritmofit.core.Callback;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthServiceImpl implements AuthService {

    @Inject
    public AuthServiceImpl() {
    }

    @Override
    public void register(AuthRequest request, Callback<AuthResponse> callback) {
        callback.onSuccess(new AuthResponse("test"));
    }

    @Override
    public void login(AuthRequest request, Callback<AuthResponse> callback) {
        callback.onSuccess(new AuthResponse("test"));
    }
}
