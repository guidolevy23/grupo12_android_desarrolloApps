package com.example.ritmofit.profile.di;

import com.example.ritmofit.profile.repository.UserRepository;
import com.example.ritmofit.profile.repository.UserRepositoryImpl;
import com.example.ritmofit.profile.service.UserService;
import com.example.ritmofit.profile.service.UserServiceImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ProfileModule {

    @Binds
    @Singleton
    public abstract UserRepository bindCourseUserRepository(UserRepositoryImpl impl);

    @Binds
    @Singleton
    public abstract UserService bindUserService(UserServiceImpl impl);
}
