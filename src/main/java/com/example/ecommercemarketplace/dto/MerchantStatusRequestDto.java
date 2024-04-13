package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantStatusRequestDto {

    private MerchantStatus status;
}
