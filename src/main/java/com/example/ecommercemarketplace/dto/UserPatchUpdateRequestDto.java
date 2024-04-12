package com.example.ecommercemarketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPatchUpdateRequestDto {

    private String firstName;

    private String lastName;

    @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$", message="Phone number should be valid")
    private String phoneNumber;
}