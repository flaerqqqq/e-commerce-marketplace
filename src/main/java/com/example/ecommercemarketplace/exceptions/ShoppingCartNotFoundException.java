package com.example.ecommercemarketplace.exceptions;

public class ShoppingCartNotFoundException extends RuntimeException{
    public ShoppingCartNotFoundException(String msg){
        super(msg);
    }
}
