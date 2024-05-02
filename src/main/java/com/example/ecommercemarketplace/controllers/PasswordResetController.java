package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.PasswordResetRequestDto;
import com.example.ecommercemarketplace.services.PasswordResetService;
import jakarta.validation.Valid;
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
    public void requestPasswordReset(@RequestBody PasswordResetRequestDto resetRequest) {
        passwordResetService.requestPasswordReset(resetRequest);
    }

    @PostMapping("/confirm-password-reset")
    public void confirmPasswordReset(@RequestBody @Valid PasswordResetConfirmationRequestDto confirmationRequest) {
        passwordResetService.confirmPasswordReset(confirmationRequest);
    }
}
