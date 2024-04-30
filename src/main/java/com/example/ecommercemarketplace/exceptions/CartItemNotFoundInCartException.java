package com.example.ecommercemarketplace.exceptions;

public class CartItemNotFoundInCartException extends RuntimeException {
    public CartItemNotFoundInCartException(String msg) {
        super(msg);
    }
}
