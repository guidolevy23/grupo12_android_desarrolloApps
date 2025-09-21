package com.example.ritmofit.di;

import android.content.Context;

import com.example.ritmofit.auth.repository.TokenRepository;
import com.example.ritmofit.auth.repository.TokenRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AuthModule {
    @Provides
    @Singleton
    public TokenRepository provideTokenRepository(@ApplicationContext Context context) {
        return new TokenRepositoryImpl(context);
    }
}
