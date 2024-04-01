package com.example.ecommercemarketplace.dto;


import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

}
