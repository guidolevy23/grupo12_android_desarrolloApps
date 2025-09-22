package com.example.ritmofit.profile.model;

public record UserResponse(
        Long id,
        String email,
        String password,
        String name,
        String photoUrl,
        String role
) {}