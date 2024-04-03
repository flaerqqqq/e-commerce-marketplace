package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.PasswordResetRequest;
import com.example.ecommercemarketplace.services.PasswordResetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PasswordResetController {

    private PasswordResetService passwordResetService;

    @PostMapping("/password-reset-request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest){

        boolean resetStatus = passwordResetService.requestPasswordReset(passwordResetRequest);

        if (resetStatus){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
