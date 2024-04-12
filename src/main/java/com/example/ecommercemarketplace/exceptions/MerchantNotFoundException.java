package com.example.ecommercemarketplace.exceptions;

public class MerchantNotFoundException extends RuntimeException{
    public MerchantNotFoundException(String msg){
        super(msg);
    }
}
