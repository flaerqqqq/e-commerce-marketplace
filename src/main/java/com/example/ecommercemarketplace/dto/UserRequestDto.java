package com.example.ecommercemarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRequestDto {

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
