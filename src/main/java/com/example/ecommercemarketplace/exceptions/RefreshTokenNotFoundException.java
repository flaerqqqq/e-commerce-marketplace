package com.example.ecommercemarketplace.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String msg){
        super(msg);
    }
}
