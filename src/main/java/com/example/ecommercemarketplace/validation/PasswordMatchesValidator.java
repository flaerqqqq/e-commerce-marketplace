package com.example.ecommercemarketplace.validation;

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
        UserRegistrationRequestDto userRegistrationRequestDto = (UserRegistrationRequestDto) o;

        return userRegistrationRequestDto.getPassword().equals(userRegistrationRequestDto.getPasswordConfirm());
    }
}