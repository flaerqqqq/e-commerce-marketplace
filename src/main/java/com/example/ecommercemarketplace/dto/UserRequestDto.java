package com.example.ecommercemarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}