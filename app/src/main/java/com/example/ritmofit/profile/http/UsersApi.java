package com.example.ritmofit.profile.http;

import com.example.ritmofit.profile.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersApi {

    @GET("/api/users/me")
    Call<UserResponse> getCurrentUser();
}
