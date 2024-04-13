package com.example.ecommercemarketplace.exceptions;

public class EmailConfirmationTokenNotFoundException extends RuntimeException {

    public EmailConfirmationTokenNotFoundException(String message) {
        super(message);
    }
}
