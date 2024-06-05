package com.example.ecommercemarketplace.exceptions;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException(String msg) {
        super(msg);
    }
}
