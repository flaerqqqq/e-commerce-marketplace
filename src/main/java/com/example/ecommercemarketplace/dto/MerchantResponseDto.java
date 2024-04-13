package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.models.enums.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MerchantResponseDto extends UserResponseDto {

    private String websiteUrl;

    private MerchantStatus status;

    private MerchantType type;

    private LocalDateTime registrationDate;
}
