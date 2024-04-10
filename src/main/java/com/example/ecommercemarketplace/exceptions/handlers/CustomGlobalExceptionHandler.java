package com.example.ecommercemarketplace.exceptions.handlers;


import com.example.ecommercemarketplace.exceptions.EmailConfirmationTokenNotFoundException;
import com.example.ecommercemarketplace.exceptions.ErrorObject;
import com.example.ecommercemarketplace.exceptions.UserAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

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

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ErrorObject> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        ErrorObject object = ErrorObject.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(object, HttpStatus.CONFLICT);
    }
}

