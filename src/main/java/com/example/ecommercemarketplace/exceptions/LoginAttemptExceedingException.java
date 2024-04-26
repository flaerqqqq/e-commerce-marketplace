package com.example.ecommercemarketplace.exceptions;

public class LoginAttemptExceedingException extends RuntimeException {
    public LoginAttemptExceedingException(String msg) {
        super(msg);
    }
}
