package com.example.ritmofit.reservas.di;

import com.example.ritmofit.reservas.repository.EnrollmentRepository;
import com.example.ritmofit.reservas.repository.EnrollmentRepositoryImpl;
import com.example.ritmofit.reservas.service.EnrollmentService;
import com.example.ritmofit.reservas.service.EnrollmentServiceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Dagger para las dependencias de inscripciones
 */
@Module
@InstallIn(SingletonComponent.class)
public abstract class EnrollmentModule {
    
    @Binds
    public abstract EnrollmentRepository bindEnrollmentRepository(
            EnrollmentRepositoryImpl enrollmentRepositoryImpl);
    
    @Binds
    public abstract EnrollmentService bindEnrollmentService(
            EnrollmentServiceImpl enrollmentServiceImpl);
}
