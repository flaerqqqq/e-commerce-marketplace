package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.models.enums.MerchantType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MerchantDto extends UserDto {

    private String websiteUrl;

    private MerchantType type;

    private MerchantStatus status;

    private LocalDateTime registrationDate;

    private List<Product> products;
}
