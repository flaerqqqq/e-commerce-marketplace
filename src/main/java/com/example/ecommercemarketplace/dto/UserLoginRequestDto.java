package com.example.ecommercemarketplace.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
