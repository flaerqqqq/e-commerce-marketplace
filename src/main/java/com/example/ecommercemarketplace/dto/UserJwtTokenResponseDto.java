package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserJwtTokenResponseDto {

    private String publicId;

    private String token;

    private String refreshToken;
}
