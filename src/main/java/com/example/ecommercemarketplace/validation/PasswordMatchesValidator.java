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
            PasswordResetConfirmationRequestDto passwordResetConfirmationRequestDto = (PasswordResetConfirmationRequestDto) o;
            try {
                return passwordResetConfirmationRequestDto.getPassword().equals(passwordResetConfirmationRequestDto.getPasswordConfirm());
            } catch (Exception e) {
                return false;
            }
        } else if (o instanceof UserRegistrationRequestDto) {
            UserRegistrationRequestDto userRegistrationRequestDto = (UserRegistrationRequestDto) o;
            try {
                return userRegistrationRequestDto.getPassword().equals(userRegistrationRequestDto.getPasswordConfirm());
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}