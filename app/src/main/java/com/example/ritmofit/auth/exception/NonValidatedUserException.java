package com.example.ritmofit.auth.exception;

public class NonValidatedUserException extends RuntimeException {

    public NonValidatedUserException() {
        super("User is not validated");
    }
}
