package com.example.ritmofit.di;

import com.example.ritmofit.services.CourseService;
import com.example.ritmofit.services.impl.CourseServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ServiceModule {

    @Binds
    @Singleton
    public abstract CourseService providePokemonService(CourseServiceImpl implementation);
}
