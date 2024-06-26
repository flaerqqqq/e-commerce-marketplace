package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class PasswordResetConfirmationRequestDto {

    private String token;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'|\\\\<>,.?/~]).*$",
            message = "Password must contain at least one uppercase letter and one special symbol")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String passwordConfirm;
}
