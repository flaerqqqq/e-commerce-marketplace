package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserResponseDto {

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
