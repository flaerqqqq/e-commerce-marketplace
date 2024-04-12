package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.MerchantType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantRegistrationRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'|\\\\<>,.?/~]).*$",
            message = "Password must contain at least one uppercase letter and one special symbol")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$", message="Phone number should be valid")
    private String phoneNumber;

    private String websiteUrl;

    @NotNull
    private MerchantType type;
}
