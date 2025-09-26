package com.example.ritmofit.security.model;

/**
 * Evento que se dispara cuando se requiere autenticación de seguridad
 * o cuando la autenticación de seguridad falla
 */
public class SecurityEvent {
    
    public enum Type {
        SECURITY_REQUIRED,      // Se requiere autenticación de seguridad
        SECURITY_FAILED,        // Falló la autenticación de seguridad
        SECURITY_SUCCESS        // Autenticación de seguridad exitosa
    }
    
    private final Type type;
    private final String message;
    
    public SecurityEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public SecurityEvent(Type type) {
        this(type, null);
    }
    
    public Type getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
}
