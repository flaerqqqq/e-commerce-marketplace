package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJwtTokenResponse {

    private String publicId;

    private String token;
}
