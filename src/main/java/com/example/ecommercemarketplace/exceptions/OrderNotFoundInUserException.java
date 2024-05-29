package com.example.ecommercemarketplace.exceptions;

public class OrderNotFoundInUserException extends RuntimeException {
    public OrderNotFoundInUserException(String msg){
        super(msg);
    }
}
