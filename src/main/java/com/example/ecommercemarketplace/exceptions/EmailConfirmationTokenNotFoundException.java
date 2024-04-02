package com.example.ecommercemarketplace.exceptions;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;

public class EmailConfirmationTokenNotFoundException extends RuntimeException{

    private String message;

    public EmailConfirmationTokenNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
