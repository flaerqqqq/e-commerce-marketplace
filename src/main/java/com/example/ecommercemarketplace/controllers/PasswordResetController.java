package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequest;
import com.example.ecommercemarketplace.dto.PasswordResetRequest;
import com.example.ecommercemarketplace.services.PasswordResetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/password-reset-request")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest){
        boolean sendStatus = passwordResetService.requestPasswordReset(passwordResetRequest);

        if (sendStatus){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/confirm-password-reset")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> confirmPasswordReset(@RequestBody PasswordResetConfirmationRequest passwordResetConfirmationRequest){
        boolean resetStatus = passwordResetService.confirmPasswordReset(passwordResetConfirmationRequest);

        if (resetStatus){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
