package com.example.ritmofit.security.di;

import android.content.Context;

import com.example.ritmofit.security.repository.SecurityRepository;
import com.example.ritmofit.security.repository.SecurityRepositoryImpl;
import com.example.ritmofit.security.service.SecurityService;
import com.example.ritmofit.security.service.SecurityServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Dagger Hilt para proporcionar las dependencias del módulo de seguridad
 */
@Module
@InstallIn(SingletonComponent.class)
public class SecurityModule {
    
    /**
     * Proporciona la implementación del repositorio de seguridad
     * @param context contexto de la aplicación
     * @return instancia del SecurityRepository
     */
    @Provides
    @Singleton
    public SecurityRepository provideSecurityRepository(@ApplicationContext Context context) {
        return new SecurityRepositoryImpl(context);
    }
    
    /**
     * Proporciona la implementación del servicio de seguridad
     * @param context contexto de la aplicación
     * @param securityRepository repositorio de seguridad
     * @return instancia del SecurityService
     */
    @Provides
    @Singleton
    public SecurityService provideSecurityService(@ApplicationContext Context context,
                                                  SecurityRepository securityRepository) {
        return new SecurityServiceImpl(context, securityRepository);
    }
}
