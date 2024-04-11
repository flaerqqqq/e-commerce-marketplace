package com.example.ecommercemarketplace.exceptions;

public class MissingAuthorizationHeaderException extends RuntimeException {
    public MissingAuthorizationHeaderException(String msg){
        super(msg);
    }
}
