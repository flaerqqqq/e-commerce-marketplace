package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.DeliveryMethod;
import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private LocalDateTime orderTime;

    private BigDecimal totalAmount;

    private int totalQuantity;

    private Map<Long, MerchantOrderStatus> statusesOfOrders;

    private DeliveryMethod deliveryMethod;
}
