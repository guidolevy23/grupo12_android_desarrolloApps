package com.example.ritmofit.security.service;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ritmofit.security.repository.SecurityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementación del servicio de seguridad que maneja la autenticación biométrica
 * y el control de intentos fallidos con redirección a configuración de PIN
 */
@Singleton
public class SecurityServiceImpl implements SecurityService {
    
    private final Context context;
    private final SecurityRepository securityRepository;
    
    @Inject
    public SecurityServiceImpl(Context context, SecurityRepository securityRepository) {
        this.context = context;
        this.securityRepository = securityRepository;
    }
    
    @Override
    public boolean isBiometricAvailable() {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | 
                                                BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
            default:
                return false;
        }
    }
    
    @Override
    public void authenticateWithBiometric(FragmentActivity activity, AuthenticationCallback callback) {
        if (!isBiometricAvailable()) {
            callback.onAuthenticationError(
                BiometricPrompt.ERROR_HW_UNAVAILABLE,
                "Autenticación biométrica no disponible"
            );
            return;
        }
        
        // Configurar el executor para ejecutar en el hilo principal
        var executor = ContextCompat.getMainExecutor(activity);
        
        // Crear el BiometricPrompt con los callbacks
        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Reiniciar contador de intentos fallidos en caso de éxito
                        securityRepository.resetFailedAttempts();
                        callback.onAuthenticationSucceeded();
                    }
                    
                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        callback.onAuthenticationFailed("Autenticación fallida. Intentá de nuevo.");
                    }
                    
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        
                        // Manejar diferentes tipos de errores
                        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED || 
                            errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            // Usuario canceló o presionó botón negativo
                            boolean shouldShowPinSettings = handleFailedAttempt();
                            if (shouldShowPinSettings) {
                                redirectToPinSettings(activity);
                            }
                        }
                        
                        callback.onAuthenticationError(errorCode, errString.toString());
                    }
                });
        
        // Configurar la información del prompt
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación requerida")
                .setSubtitle("Usá tu huella digital, reconocimiento facial o PIN del dispositivo")
                .setDescription("Autenticá tu identidad para acceder a RitmoFit")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK |
                                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
        
        // Mostrar el prompt de autenticación
        biometricPrompt.authenticate(promptInfo);
    }
    
    @Override
    public boolean handleFailedAttempt() {
        securityRepository.incrementFailedAttempts();
        return securityRepository.hasReachedMaxAttempts();
    }
    
    @Override
    public void resetFailedAttempts() {
        securityRepository.resetFailedAttempts();
    }
    
    @Override
    public boolean shouldRequestAuthentication() {
        return securityRepository.isSecurityEnabled();
    }
    
    /**
     * Redirige al usuario a la configuración de seguridad para cambiar el PIN
     * @param activity la actividad desde donde se hace la redirección
     */
    private void redirectToPinSettings(FragmentActivity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            // Si no se puede abrir la configuración de seguridad, intentar con configuración general
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivity(intent);
            } catch (Exception ex) {
                // En caso de error, notificar al callback
                // Este caso es muy raro pero puede ocurrir en algunos dispositivos
            }
        }
    }
}
