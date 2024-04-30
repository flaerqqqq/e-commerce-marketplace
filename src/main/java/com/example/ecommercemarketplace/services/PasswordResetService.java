package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.PasswordResetRequestDto;

public interface PasswordResetService {

    void requestPasswordReset(PasswordResetRequestDto passwordResetRequestDto);

    void confirmPasswordReset(PasswordResetConfirmationRequestDto passwordResetConfirmationRequestDto);

    void deleteExpiredTokens();
}
