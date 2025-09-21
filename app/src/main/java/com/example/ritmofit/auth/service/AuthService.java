package com.example.ritmofit.auth.service;

import com.example.ritmofit.auth.model.LoginRequest;
import com.example.ritmofit.auth.model.LoginResponse;
import com.example.ritmofit.auth.model.RegisterRequest;
import com.example.ritmofit.auth.model.RegisterResponse;
import com.example.ritmofit.core.DomainCallback;

public interface AuthService {

    void register(RegisterRequest request, DomainCallback<RegisterResponse> callback);
    void login(LoginRequest request, DomainCallback<LoginResponse> domainCallback);
}
