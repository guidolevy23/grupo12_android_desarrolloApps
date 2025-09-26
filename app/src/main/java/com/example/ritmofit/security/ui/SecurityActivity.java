package com.example.ritmofit.security.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.example.ritmofit.MainActivity;
import com.example.ritmofit.R;
import com.example.ritmofit.security.model.SecurityEvent;
import com.example.ritmofit.security.service.SecurityService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Actividad que maneja la autenticación de seguridad biométrica
 * Se muestra después del login exitoso y antes de acceder a la aplicación principal
 */
@AndroidEntryPoint
public class SecurityActivity extends AppCompatActivity {
    
    @Inject
    SecurityService securityService;
    
    private TextView titleTextView;
    private TextView subtitleTextView;
    private Button authenticateButton;
    private TextView attemptsTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        
        initializeViews();
        setupClickListeners();
        updateUI();
        
        // Iniciar automáticamente la autenticación si está disponible
        if (securityService.isBiometricAvailable()) {
            startBiometricAuthentication();
        } else {
            showBiometricNotAvailable();
        }
    }
    
    private void initializeViews() {
        titleTextView = findViewById(R.id.security_title);
        subtitleTextView = findViewById(R.id.security_subtitle);
        authenticateButton = findViewById(R.id.authenticate_button);
        attemptsTextView = findViewById(R.id.attempts_text);
    }
    
    private void setupClickListeners() {
        authenticateButton.setOnClickListener(v -> startBiometricAuthentication());
    }
    
    private void updateUI() {
        titleTextView.setText("Autenticación requerida");
        subtitleTextView.setText("Para tu seguridad, necesitamos verificar tu identidad antes de continuar");
        authenticateButton.setText("Autenticar");
        
        // Mostrar número de intentos si hay intentos fallidos
        // (esto se actualiza en handleAuthenticationFailed)
    }
    
    private void startBiometricAuthentication() {
        securityService.authenticateWithBiometric(this, new SecurityService.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded() {
                handleAuthenticationSuccess();
            }
            
            @Override
            public void onAuthenticationFailed(String error) {
                handleAuthenticationFailed(error);
            }
            
            @Override
            public void onAuthenticationError(int errorCode, String errorMessage) {
                handleAuthenticationError(errorCode, errorMessage);
            }
        });
    }
    
    private void handleAuthenticationSuccess() {
        Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show();
        
        // Notificar éxito y navegar a MainActivity
        EventBus.getDefault().post(new SecurityEvent(SecurityEvent.Type.SECURITY_SUCCESS));
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("SECURITY_AUTHENTICATED", true);
        startActivity(intent);
        finish();
    }
    
    private void handleAuthenticationFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        
        // Actualizar UI para mostrar el botón de reintento
        authenticateButton.setText("Reintentar autenticación");
        authenticateButton.setEnabled(true);
        
        // Manejar intento fallido
        boolean shouldShowPinSettings = securityService.handleFailedAttempt();
        if (shouldShowPinSettings) {
            showMaxAttemptsReached();
        }
    }
    
    private void handleAuthenticationError(int errorCode, String errorMessage) {
        // Diferentes maneras de manejar errores según el código
        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
            // Usuario canceló - permitir reintento
            authenticateButton.setText("Reintentar autenticación");
            authenticateButton.setEnabled(true);
        } else if (errorCode == BiometricPrompt.ERROR_HW_UNAVAILABLE) {
            // Hardware no disponible
            showBiometricNotAvailable();
        } else {
            // Otros errores - permitir reintento
            Toast.makeText(this, "Error de autenticación: " + errorMessage, Toast.LENGTH_LONG).show();
            authenticateButton.setText("Reintentar autenticación");
            authenticateButton.setEnabled(true);
        }
    }
    
    private void showBiometricNotAvailable() {
        titleTextView.setText("Autenticación no disponible");
        subtitleTextView.setText("Tu dispositivo no tiene configurada la autenticación biométrica o PIN. " +
                "Por favor, configurá la seguridad de tu dispositivo en Configuración.");
        authenticateButton.setText("Abrir configuración");
        
        authenticateButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir la configuración", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showMaxAttemptsReached() {
        titleTextView.setText("Límite de intentos alcanzado");
        subtitleTextView.setText("Fallaste demasiadas veces. Por tu seguridad, " +
                "necesitás cambiar tu PIN en la configuración del dispositivo.");
        authenticateButton.setText("Ir a configuración");
        attemptsTextView.setText("Intentos máximos alcanzados");
        
        authenticateButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
                // Después de ir a configuración, cerrar la app por seguridad
                finishAffinity();
            } catch (Exception e) {
                Toast.makeText(this, "No se pudo abrir la configuración", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        // Prevenir que el usuario pueda volver atrás sin autenticarse
        // En su lugar, minimizar la aplicación
        moveTaskToBack(true);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Si el usuario vuelve desde configuración, reiniciar intentos y permitir autenticación
        if (securityService.isBiometricAvailable()) {
            securityService.resetFailedAttempts();
            updateUI();
            authenticateButton.setText("Autenticar");
            authenticateButton.setOnClickListener(v -> startBiometricAuthentication());
        }
    }
}
