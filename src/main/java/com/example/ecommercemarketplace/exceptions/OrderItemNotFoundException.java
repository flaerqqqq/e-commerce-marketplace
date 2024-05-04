package com.example.ecommercemarketplace.exceptions;

public class OrderItemNotFoundException extends RuntimeException{
    public OrderItemNotFoundException(String msg){
        super(msg);
    }
}
