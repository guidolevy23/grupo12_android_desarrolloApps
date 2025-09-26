package com.example.ritmofit.security.repository;

/**
 * Repositorio para manejar las preferencias de seguridad de la aplicación
 */
public interface SecurityRepository {
    
    /**
     * Verifica si la autenticación de seguridad está habilitada
     * @return true si está habilitada, false en caso contrario
     */
    boolean isSecurityEnabled();
    
    /**
     * Habilita o deshabilita la autenticación de seguridad
     * @param enabled true para habilitar, false para deshabilitar
     */
    void setSecurityEnabled(boolean enabled);
    
    /**
     * Obtiene el número de intentos fallidos de autenticación
     * @return número de intentos fallidos
     */
    int getFailedAttempts();
    
    /**
     * Incrementa el contador de intentos fallidos
     */
    void incrementFailedAttempts();
    
    /**
     * Reinicia el contador de intentos fallidos
     */
    void resetFailedAttempts();
    
    /**
     * Verifica si se ha alcanzado el límite máximo de intentos fallidos
     * @return true si se alcanzó el límite, false en caso contrario
     */
    boolean hasReachedMaxAttempts();
    
    /**
     * Obtiene el límite máximo de intentos fallidos permitidos
     * @return número máximo de intentos
     */
    int getMaxAttempts();
}
