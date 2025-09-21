package com.example.ritmofit.auth.di;

import com.example.ritmofit.auth.service.AuthService;
import com.example.ritmofit.auth.service.AuthServiceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AuthModule {

    @Binds
    @Singleton
    public abstract AuthService bindAuthService(AuthServiceImpl impl);
}