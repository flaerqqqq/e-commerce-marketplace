package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrderStatusRequestDto {

    private MerchantOrderStatus status;
}
