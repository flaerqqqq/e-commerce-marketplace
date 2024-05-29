package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequestDto {

    private PaymentMethod paymentMethod;

    private DeliveryDataRequestDto deliveryData;
}
