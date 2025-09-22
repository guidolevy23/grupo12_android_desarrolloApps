package com.example.ritmofit.profile.repository;

import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.http.UsersApi;
import com.example.ritmofit.profile.model.User;
import com.example.ritmofit.profile.model.UserResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {

    private final UsersApi api;

    @Inject
    public UserRepositoryImpl(UsersApi api) {
        this.api = api;
    }
    @Override
    public void currentUser(DomainCallback<User> callback) {
        Call<UserResponse> call = api.getCurrentUser();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onError(new Exception("Error al obtener el usuario"));
                    return;
                }
                UserResponse userResponse = response.body();
                User user = new User(
                        userResponse.id(),
                        userResponse.email(),
                        userResponse.password(),
                        userResponse.name(),
                        userResponse.photoUrl(),
                        userResponse.role()
                );
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onError(new Exception("Error de red al obtener el usuario", t));
            }
        });
    }
}
