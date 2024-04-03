package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequest;
import com.example.ecommercemarketplace.dto.PasswordResetRequest;

public interface PasswordResetService {

    boolean requestPasswordReset(PasswordResetRequest passwordResetRequest);

    boolean confirmPasswordReset(PasswordResetConfirmationRequest passwordResetConfirmationRequest);
}
