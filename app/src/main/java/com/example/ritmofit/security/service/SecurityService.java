package com.example.ritmofit.security.service;

import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

/**
 * Servicio para manejar la autenticación biométrica y de PIN
 */
public interface SecurityService {
    
    /**
     * Callback para los resultados de autenticación
     */
    interface AuthenticationCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationFailed(String error);
        void onAuthenticationError(int errorCode, String errorMessage);
    }
    
    /**
     * Verifica si la autenticación biométrica está disponible en el dispositivo
     * @return true si está disponible, false en caso contrario
     */
    boolean isBiometricAvailable();
    
    /**
     * Inicia el proceso de autenticación biométrica
     * @param activity la actividad desde donde se llama
     * @param callback callback para recibir los resultados
     */
    void authenticateWithBiometric(FragmentActivity activity, AuthenticationCallback callback);
    
    /**
     * Maneja un intento fallido de autenticación
     * @return true si se debe mostrar configuración de PIN, false en caso contrario
     */
    boolean handleFailedAttempt();
    
    /**
     * Reinicia el contador de intentos fallidos
     */
    void resetFailedAttempts();
    
    /**
     * Verifica si se debe solicitar autenticación de seguridad
     * @return true si se debe solicitar, false en caso contrario
     */
    boolean shouldRequestAuthentication();
}
