package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    private LoginData loginData;

    private List<AddressDto> addresses;

    private List<OrderDto> orders;

    private List<ProductReviewDto> productReviews;
}
