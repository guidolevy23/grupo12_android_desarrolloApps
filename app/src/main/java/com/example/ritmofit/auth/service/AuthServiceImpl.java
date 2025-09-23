package com.example.ritmofit.auth.service;

import androidx.annotation.NonNull;

import com.example.ritmofit.auth.http.api.LoginRequest;
import com.example.ritmofit.auth.http.api.RegisterRequest;
import com.example.ritmofit.auth.model.Login;
import com.example.ritmofit.auth.http.api.LoginResponse;
import com.example.ritmofit.auth.model.Register;
import com.example.ritmofit.auth.http.api.RegisterResponse;
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
    public void register(Register register, DomainCallback<String> domainCallback) {
        RegisterRequest request = new RegisterRequest(
                register.username(),
                register.password(),
                register.name()
        );
        Call<RegisterResponse> call = api.register(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call,
                                   @NonNull Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    domainCallback.onError(new Exception("Error al registrar usuario"));
                    return;
                }
                domainCallback.onSuccess(response.body().message());
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call,
                                  @NonNull Throwable t) {
                domainCallback.onError(new Exception("Error de red al registrar usuario", t));
            }
        });
    }

    @Override
    public void login(Login login, DomainCallback<String> callback) {
        LoginRequest request = new LoginRequest(
                login.username(),
                login.password()
        );
       Call<LoginResponse> call = api.login(request);
       call.enqueue(new Callback<>() {
              @Override
              public void onResponse(@NonNull Call<LoginResponse> call,
                                     @NonNull Response<LoginResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                     callback.onError(new Exception("Error al iniciar sesión"));
                     return;
                }
                callback.onSuccess(response.body().token());
              }

              @Override
              public void onFailure(@NonNull Call<LoginResponse> call,
                                    @NonNull Throwable t) {
                  callback.onError(new Exception("Error de red al iniciar sesión", t));
              }
       });
    }
}
