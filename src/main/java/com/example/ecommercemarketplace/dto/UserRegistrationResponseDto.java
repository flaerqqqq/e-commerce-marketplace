package com.example.ecommercemarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserRegistrationResponseDto {

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
