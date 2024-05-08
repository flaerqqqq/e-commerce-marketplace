package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantOrderResponseDto {

    private Long id;

    private List<OrderItemResponseDto> orderItems;

    private BigDecimal totalAmount;

    private MerchantOrderStatus status;

    private Long parentOrderId;
}
