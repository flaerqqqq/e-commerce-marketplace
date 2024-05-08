package com.example.ecommercemarketplace.exceptions;

public class MerchantOrderNotFoundInMerchantException extends RuntimeException{
    public MerchantOrderNotFoundInMerchantException(String msg) {
        super(msg);
    }
}
