package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.MerchantType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MerchantUpdateRequestDto extends UserUpdateRequestDto {

    @NotNull(message = "Merchant type is required")
    private MerchantType type;

    @NotBlank(message = "Website URL is required")
    private String websiteUrl;
}
