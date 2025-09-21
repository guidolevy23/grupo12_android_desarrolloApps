package com.example.ritmofit.home.di;

import com.example.ritmofit.home.http.CoursesApi;

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
    public CoursesApi provideCoursesApi(Retrofit retrofit) {
        return retrofit.create(CoursesApi.class);
    }
}
