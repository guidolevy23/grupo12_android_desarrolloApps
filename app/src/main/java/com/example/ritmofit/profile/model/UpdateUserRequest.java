package com.example.ritmofit.profile.model;

public class UpdateUserRequest {
    public String name;
    public String direccion;
    public String telefono;
    public String photoUrl;
    public String password;
    public String role;

    public String email;
    public Long id;

    public UpdateUserRequest(String name, String direccion, String telefono, String photoUrl, String password, String role, String email, Long id) {
        this.name = name;
        this.direccion = direccion;
        this.telefono = telefono;
        this.photoUrl = photoUrl;
        this.password = password;
        this.role = role;
        this.email = email;
        this.id = id;
    }
}
