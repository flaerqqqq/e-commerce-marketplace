package com.example.ecommercemarketplace.exceptions;

public class RefreshTokenAlreadyExpired extends RuntimeException {
    public RefreshTokenAlreadyExpired(String msg){
        super(msg);
    }
}
