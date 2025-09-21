package com.example.ritmofit.data.api.model.auth;

public class AuthResponseDTO {
    private String message;
    private String token;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
