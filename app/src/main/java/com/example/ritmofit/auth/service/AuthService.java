package com.example.ritmofit.auth.service;

import com.example.ritmofit.auth.model.Login;
import com.example.ritmofit.auth.model.Register;
import com.example.ritmofit.core.DomainCallback;

public interface AuthService {

    void register(Register register, DomainCallback<String> callback);
    void login(Login login, DomainCallback<String> domainCallback);
}
