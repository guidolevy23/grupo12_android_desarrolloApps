package com.example.ritmofit.di;

import com.example.ritmofit.data.repository.CourseRepository;
import com.example.ritmofit.data.repository.impl.CourseRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract CourseRepository providePokemonRepository(CourseRepositoryImpl implementation);
}