package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusChangeRequestDto {

    @NotNull(message = "Status should not be null")
    private MerchantOrderStatus status;
}
