package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.PasswordResetRequestDto;

public interface PasswordResetService {

    boolean requestPasswordReset(PasswordResetRequestDto passwordResetRequestDto);

    boolean confirmPasswordReset(PasswordResetConfirmationRequestDto passwordResetConfirmationRequestDto);

    void deleteExpiredTokens();
}
