package com.example.ecommercemarketplace.exceptions;

public class MerchantAlreadyExistsException extends RuntimeException{
    public MerchantAlreadyExistsException(String msg){
        super(msg);
    }
}
