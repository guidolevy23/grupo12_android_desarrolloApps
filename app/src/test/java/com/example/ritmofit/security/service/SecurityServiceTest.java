package com.example.ritmofit.security.service;

import android.content.Context;

import androidx.biometric.BiometricManager;

import com.example.ritmofit.security.repository.SecurityRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario para SecurityServiceImpl
 * Verifica la lógica de autenticación biométrica y manejo de intentos fallidos
 */
@RunWith(RobolectricTestRunner.class)
public class SecurityServiceTest {

    @Mock
    private Context mockContext;

    @Mock
    private SecurityRepository mockSecurityRepository;

    @Mock
    private BiometricManager mockBiometricManager;

    private SecurityService securityService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        securityService = new SecurityServiceImpl(mockContext, mockSecurityRepository);
    }

    @Test
    public void testShouldRequestAuthentication_WhenSecurityEnabled() {
        // Arrange
        when(mockSecurityRepository.isSecurityEnabled()).thenReturn(true);

        // Act
        boolean result = securityService.shouldRequestAuthentication();

        // Assert
        assertTrue("Debe solicitar autenticación cuando la seguridad está habilitada", result);
    }

    @Test
    public void testShouldRequestAuthentication_WhenSecurityDisabled() {
        // Arrange
        when(mockSecurityRepository.isSecurityEnabled()).thenReturn(false);

        // Act
        boolean result = securityService.shouldRequestAuthentication();

        // Assert
        assertFalse("No debe solicitar autenticación cuando la seguridad está deshabilitada", result);
    }

    @Test
    public void testHandleFailedAttempt_BelowMaxAttempts() {
        // Arrange
        when(mockSecurityRepository.hasReachedMaxAttempts()).thenReturn(false);

        // Act
        boolean result = securityService.handleFailedAttempt();

        // Assert
        verify(mockSecurityRepository).incrementFailedAttempts();
        assertFalse("No debe mostrar configuración de PIN si no se alcanzó el límite", result);
    }

    @Test
    public void testHandleFailedAttempt_ReachedMaxAttempts() {
        // Arrange
        when(mockSecurityRepository.hasReachedMaxAttempts()).thenReturn(true);

        // Act
        boolean result = securityService.handleFailedAttempt();

        // Assert
        verify(mockSecurityRepository).incrementFailedAttempts();
        assertTrue("Debe mostrar configuración de PIN cuando se alcanza el límite", result);
    }

    @Test
    public void testResetFailedAttempts() {
        // Act
        securityService.resetFailedAttempts();

        // Assert
        verify(mockSecurityRepository).resetFailedAttempts();
    }

    @Test
    public void testIsBiometricAvailable_Success() {
        // Arrange
        try (MockedStatic<BiometricManager> biometricManagerStatic = mockStatic(BiometricManager.class)) {
            biometricManagerStatic.when(() -> BiometricManager.from(mockContext)).thenReturn(mockBiometricManager);
            when(mockBiometricManager.canAuthenticate(anyInt())).thenReturn(BiometricManager.BIOMETRIC_SUCCESS);

            // Act
            boolean result = securityService.isBiometricAvailable();

            // Assert
            assertTrue("Debe retornar true cuando la biometría está disponible", result);
        }
    }

    @Test
    public void testIsBiometricAvailable_NoHardware() {
        // Arrange
        try (MockedStatic<BiometricManager> biometricManagerStatic = mockStatic(BiometricManager.class)) {
            biometricManagerStatic.when(() -> BiometricManager.from(mockContext)).thenReturn(mockBiometricManager);
            when(mockBiometricManager.canAuthenticate(anyInt())).thenReturn(BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE);

            // Act
            boolean result = securityService.isBiometricAvailable();

            // Assert
            assertFalse("Debe retornar false cuando no hay hardware biométrico", result);
        }
    }

    @Test
    public void testIsBiometricAvailable_NoneEnrolled() {
        // Arrange
        try (MockedStatic<BiometricManager> biometricManagerStatic = mockStatic(BiometricManager.class)) {
            biometricManagerStatic.when(() -> BiometricManager.from(mockContext)).thenReturn(mockBiometricManager);
            when(mockBiometricManager.canAuthenticate(anyInt())).thenReturn(BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED);

            // Act
            boolean result = securityService.isBiometricAvailable();

            // Assert
            assertFalse("Debe retornar false cuando no hay biometrías registradas", result);
        }
    }
}
