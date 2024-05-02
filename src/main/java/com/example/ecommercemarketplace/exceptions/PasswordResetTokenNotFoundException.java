package com.example.ecommercemarketplace.exceptions;

public class PasswordResetTokenNotFoundException extends RuntimeException{
    public PasswordResetTokenNotFoundException(String msg){
        super(msg);
    }
}
