package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private boolean isEnabled;

    private EmailConfirmationToken emailConfirmationToken;

    private List<Role> roles;
}
