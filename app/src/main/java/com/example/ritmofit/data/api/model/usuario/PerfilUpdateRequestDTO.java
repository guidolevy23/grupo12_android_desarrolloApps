package com.example.ritmofit.data.api.model.usuario;

public class PerfilUpdateRequestDTO {
    private String nombre;
    private String fotoUrl;

    public PerfilUpdateRequestDTO() {}

    public PerfilUpdateRequestDTO(String nombre, String fotoUrl) {
        this.nombre = nombre;
        this.fotoUrl = fotoUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
