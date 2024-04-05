package com.example.ecommercemarketplace.utils;

import lombok.Getter;
import lombok.Setter;

public enum ErrorMessages {
    RECORD_NOT_FOUND("Record with provided id is not found"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address is not verified"),
    AUTHENTICATION_FAILED("Authentication failed");

    @Setter
    @Getter
    private String errorMessage;

    private ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }



}
