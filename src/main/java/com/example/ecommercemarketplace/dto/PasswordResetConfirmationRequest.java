package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmationRequest {

    private String token;

    private String password;
}
