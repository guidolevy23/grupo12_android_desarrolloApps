package com.example.ritmofit.home.di;

import com.example.ritmofit.home.repository.CourseRepository;
import com.example.ritmofit.home.repository.CourseRepositoryImpl;
import com.example.ritmofit.home.service.CourseService;
import com.example.ritmofit.home.service.CourseServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class HomeModule {

    @Binds
    @Singleton
    public abstract CourseRepository bindCourseRepository(CourseRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract CourseService bindCourseService(CourseServiceImpl impl);
}