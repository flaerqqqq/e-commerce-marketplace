package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.enums.DeliveryMethod;
import com.example.ecommercemarketplace.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.DataSizeUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private LocalDateTime orderTime;

    private BigDecimal totalAmount;

    private int totalQuantity;

    private OrderStatus status;

    private DeliveryMethod deliveryMethod;
}
