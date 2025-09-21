package com.example.ritmofit.auth.service;

import androidx.annotation.NonNull;

import com.example.ritmofit.auth.model.LoginRequest;
import com.example.ritmofit.auth.model.LoginResponse;
import com.example.ritmofit.auth.model.RegisterRequest;
import com.example.ritmofit.auth.model.RegisterResponse;
import com.example.ritmofit.auth.http.AuthApi;
import com.example.ritmofit.core.DomainCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AuthServiceImpl implements AuthService {

    private final AuthApi api;

    @Inject
    public AuthServiceImpl(AuthApi api) {
        this.api = api;
    }

    @Override
    public void register(RegisterRequest request, DomainCallback<RegisterResponse> domainCallback) {
        domainCallback.onSuccess(new RegisterResponse("test"));
    }

    @Override
    public void login(LoginRequest request, DomainCallback<LoginResponse> callback) {
       Call<LoginResponse> call = api.login(request);
       call.enqueue(new Callback<>() {
              @Override
              public void onResponse(@NonNull Call<LoginResponse> call,
                                     @NonNull Response<LoginResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                     callback.onError(new Exception("Error al iniciar sesión"));
                     return;
                }
                callback.onSuccess(response.body());
              }

              @Override
              public void onFailure(@NonNull Call<LoginResponse> call,
                                    @NonNull Throwable t) {
                  callback.onError(new Exception("Error de red al iniciar sesión", t));
              }
       });
    }
}
