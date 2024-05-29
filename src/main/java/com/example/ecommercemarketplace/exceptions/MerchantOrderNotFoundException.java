package com.example.ecommercemarketplace.exceptions;

public class MerchantOrderNotFoundException extends RuntimeException{
    public MerchantOrderNotFoundException(String msg){
        super(msg);
    }
}
