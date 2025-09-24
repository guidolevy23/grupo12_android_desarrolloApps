package com.example.ritmofit.profile.model;

public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String photoUrl;
    private String role;
    private String telefono;
    private String direccion;

    public User(Long id, String email, String password, String name, String photoUrl, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.photoUrl = photoUrl;
        this.role = role;
    }

    public User(Long id, String email, String password, String name, String photoUrl, String role, String direccion, String telefono) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.photoUrl = photoUrl;
        this.role = role;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
