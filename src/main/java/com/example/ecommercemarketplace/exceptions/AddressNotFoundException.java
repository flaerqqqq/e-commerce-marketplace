package com.example.ecommercemarketplace.exceptions;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String msg){
        super(msg);
    }
}
