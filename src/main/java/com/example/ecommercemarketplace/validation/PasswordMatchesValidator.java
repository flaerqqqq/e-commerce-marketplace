package com.example.ecommercemarketplace.validation;

import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof PasswordResetConfirmationRequestDto) {
            return isValidPasswordMatch(((PasswordResetConfirmationRequestDto) o).getPassword(), ((PasswordResetConfirmationRequestDto) o).getPasswordConfirm());
        } else if (o instanceof UserRegistrationRequestDto) {
            return isValidPasswordMatch(((UserRegistrationRequestDto) o).getPassword(), ((UserRegistrationRequestDto) o).getPasswordConfirm());
        }
        return false;
    }

    private boolean isValidPasswordMatch(String password, String passwordConfirm) {
        try {
            return password != null && password.equals(passwordConfirm);
        } catch (Exception e) {
            return false;
        }
    }
}