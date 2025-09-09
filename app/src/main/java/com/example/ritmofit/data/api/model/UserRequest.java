package com.example.ritmofit.data.api.model;

public class UserRequest {
    public String nombre;
    public String email;
    public String foto; // opcional (URL/Base64)

    public UserRequest(String nombre, String email, String foto) {
        this.nombre = nombre;
        this.email = email;
        this.foto = foto;
    }
}
