package com.example.ecommercemarketplace.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String msg){
        super(msg);
    }
}
