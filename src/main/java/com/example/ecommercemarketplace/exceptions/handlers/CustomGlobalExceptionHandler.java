package com.example.ecommercemarketplace.exceptions.handlers;


import com.example.ecommercemarketplace.exceptions.EmailConfirmationTokenNotFoundException;
import com.example.ecommercemarketplace.exceptions.ErrorObject;
import com.example.ecommercemarketplace.exceptions.UserAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            EmailConfirmationTokenNotFoundException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<ErrorObject> handleUserNotFoundException(RuntimeException ex, WebRequest webRequest){
        ErrorObject object = ErrorObject.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleSideExceptions(Exception exception,  WebRequest request){
        ErrorObject errorObject = ErrorObject.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError)error).getField();
            String errorName = error.getDefaultMessage();
            errors.put(fieldName, errorName);
        });

        ErrorObject errorObject = ErrorObject.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errors)
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
}

