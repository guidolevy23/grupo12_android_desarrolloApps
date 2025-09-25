package com.example.ritmofit.security.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementación del repositorio de seguridad usando EncryptedSharedPreferences
 * para almacenar de forma segura las configuraciones de autenticación
 */
@Singleton
public class SecurityRepositoryImpl implements SecurityRepository {
    
    private static final String SECURITY_PREFS_FILE = "security_preferences";
    private static final String KEY_SECURITY_ENABLED = "security_enabled";
    private static final String KEY_FAILED_ATTEMPTS = "failed_attempts";
    private static final int MAX_ATTEMPTS = 3;
    
    private final SharedPreferences sharedPreferences;
    
    @Inject
    public SecurityRepositoryImpl(Context context) {
        try {
            // Crear MasterKey para cifrado
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            
            // Crear EncryptedSharedPreferences
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    SECURITY_PREFS_FILE,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar las preferencias de seguridad", e);
        }
    }
    
    @Override
    public boolean isSecurityEnabled() {
        // Por defecto, la seguridad está habilitada
        return sharedPreferences.getBoolean(KEY_SECURITY_ENABLED, true);
    }
    
    @Override
    public void setSecurityEnabled(boolean enabled) {
        sharedPreferences.edit()
                .putBoolean(KEY_SECURITY_ENABLED, enabled)
                .apply();
    }
    
    @Override
    public int getFailedAttempts() {
        return sharedPreferences.getInt(KEY_FAILED_ATTEMPTS, 0);
    }
    
    @Override
    public void incrementFailedAttempts() {
        int currentAttempts = getFailedAttempts();
        sharedPreferences.edit()
                .putInt(KEY_FAILED_ATTEMPTS, currentAttempts + 1)
                .apply();
    }
    
    @Override
    public void resetFailedAttempts() {
        sharedPreferences.edit()
                .putInt(KEY_FAILED_ATTEMPTS, 0)
                .apply();
    }
    
    @Override
    public boolean hasReachedMaxAttempts() {
        return getFailedAttempts() >= MAX_ATTEMPTS;
    }
    
    @Override
    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }
}
