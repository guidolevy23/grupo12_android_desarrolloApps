package com.example.ritmofit.di;

import android.content.Context;

import com.example.ritmofit.data.api.model.HistorialService;
import com.example.ritmofit.home.repository.CourseRepository;
import com.example.ritmofit.data.repository.HistorialRepository;
import com.example.ritmofit.home.repository.CourseRepositoryImpl;
import com.example.ritmofit.data.repository.impl.HistorialRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {


    @Provides
    @Singleton
    public static HistorialRepository provideHistorialRepository(
            HistorialService historialService, 
            @ApplicationContext Context context) {
        return new HistorialRepositoryImpl(historialService, context);
    }
}