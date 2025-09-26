package com.example.ritmofit.profile.http;

import com.example.ritmofit.profile.model.UpdateUserRequest;
import com.example.ritmofit.profile.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsersApi {

    @GET("/api/users/me")
    Call<UserResponse> getCurrentUser();

    @PUT("/api/users/{id}")
    Call<UserResponse> updateById(@Path("id") Long id, @Body UpdateUserRequest body);
}
