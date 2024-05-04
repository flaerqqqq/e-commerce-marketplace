package com.example.ecommercemarketplace.exceptions;

public class EmptyShoppingCartException extends RuntimeException {
    public EmptyShoppingCartException(String msg){
        super(msg);
    }
}
