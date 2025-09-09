package com.example.ritmofit.data.api.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/{id}")
    Call<UserResponse> getUser(@Path("id") Long id);

    @PUT("user/{id}")
    Call<UserResponse> updateUser(@Path("id") Long id, @Body UserRequest request);
}
