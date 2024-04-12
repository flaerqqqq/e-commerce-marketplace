package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantRegistrationResponseDto {

    private String publicId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private MerchantType type;

}
