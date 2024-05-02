package com.example.ecommercemarketplace.exceptions;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String msg){
        super(msg);
    }
}
