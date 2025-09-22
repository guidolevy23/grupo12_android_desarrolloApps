package com.example.ritmofit.profile.di;

import com.example.ritmofit.profile.http.UsersApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public UsersApi providecomUsersApi(Retrofit retrofit) {
        return retrofit.create(UsersApi.class);
    }
}
