package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.models.Role;
import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.models.enums.MerchantType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantDto {

    private Long id;

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private String websiteUrl;

    private MerchantType type;

    private MerchantStatus status;

    private LocalDateTime registrationDate;

    private List<Product> products;

    private boolean isEnabled;

    private EmailConfirmationToken emailConfirmationToken;

    private List<Role> roles;
}
