package com.example.ecommercemarketplace.exceptions;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;

public class EmailConfirmationTokenNotFoundException extends RuntimeException{

    public EmailConfirmationTokenNotFoundException(String message){
        super(message);
    }
}
