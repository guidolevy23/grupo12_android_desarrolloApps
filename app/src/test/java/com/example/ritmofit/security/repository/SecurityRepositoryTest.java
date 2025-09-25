package com.example.ritmofit.security.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test unitario para SecurityRepositoryImpl
 * Verifica el comportamiento del repositorio de seguridad
 */
@RunWith(RobolectricTestRunner.class)
public class SecurityRepositoryTest {

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private SecurityRepository securityRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar mocks
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), any(Boolean.class))).thenReturn(mockEditor);
        when(mockEditor.putInt(anyString(), any(Integer.class))).thenReturn(mockEditor);

        // Mock EncryptedSharedPreferences creation
        try (MockedStatic<EncryptedSharedPreferences> encryptedPrefs = mockStatic(EncryptedSharedPreferences.class);
             MockedStatic<MasterKey.Builder> masterKeyBuilder = mockStatic(MasterKey.Builder.class)) {

            MasterKey mockMasterKey = mock(MasterKey.class);
            MasterKey.Builder mockBuilder = mock(MasterKey.Builder.class);

            when(masterKeyBuilder.getMock().setKeyScheme(any())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(mockMasterKey);

            encryptedPrefs.when(() -> EncryptedSharedPreferences.create(
                    eq(mockContext),
                    anyString(),
                    eq(mockMasterKey),
                    any(),
                    any()
            )).thenReturn(mockSharedPreferences);

            securityRepository = new SecurityRepositoryImpl(mockContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsSecurityEnabledDefaultValue() {
        // Arrange
        when(mockSharedPreferences.getBoolean("security_enabled", true)).thenReturn(true);

        // Act
        boolean result = securityRepository.isSecurityEnabled();

        // Assert
        assertTrue("La seguridad debe estar habilitada por defecto", result);
    }

    @Test
    public void testSetSecurityEnabled() {
        // Act
        securityRepository.setSecurityEnabled(false);

        // Assert
        verify(mockEditor).putBoolean("security_enabled", false);
        verify(mockEditor).apply();
    }

    @Test
    public void testGetFailedAttemptsDefaultValue() {
        // Arrange
        when(mockSharedPreferences.getInt("failed_attempts", 0)).thenReturn(0);

        // Act
        int result = securityRepository.getFailedAttempts();

        // Assert
        assertEquals("Los intentos fallidos deben ser 0 por defecto", 0, result);
    }

    @Test
    public void testIncrementFailedAttempts() {
        // Arrange
        when(mockSharedPreferences.getInt("failed_attempts", 0)).thenReturn(1);

        // Act
        securityRepository.incrementFailedAttempts();

        // Assert
        verify(mockEditor).putInt("failed_attempts", 2);
        verify(mockEditor).apply();
    }

    @Test
    public void testResetFailedAttempts() {
        // Act
        securityRepository.resetFailedAttempts();

        // Assert
        verify(mockEditor).putInt("failed_attempts", 0);
        verify(mockEditor).apply();
    }

    @Test
    public void testHasReachedMaxAttempts() {
        // Arrange - simular 3 intentos fallidos (máximo)
        when(mockSharedPreferences.getInt("failed_attempts", 0)).thenReturn(3);

        // Act
        boolean result = securityRepository.hasReachedMaxAttempts();

        // Assert
        assertTrue("Debe retornar true cuando se alcanzan 3 intentos fallidos", result);
    }

    @Test
    public void testHasNotReachedMaxAttempts() {
        // Arrange - simular 2 intentos fallidos (menos del máximo)
        when(mockSharedPreferences.getInt("failed_attempts", 0)).thenReturn(2);

        // Act
        boolean result = securityRepository.hasReachedMaxAttempts();

        // Assert
        assertFalse("Debe retornar false cuando no se alcanzan los intentos máximos", result);
    }

    @Test
    public void testGetMaxAttempts() {
        // Act
        int maxAttempts = securityRepository.getMaxAttempts();

        // Assert
        assertEquals("El máximo de intentos debe ser 3", 3, maxAttempts);
    }
}
