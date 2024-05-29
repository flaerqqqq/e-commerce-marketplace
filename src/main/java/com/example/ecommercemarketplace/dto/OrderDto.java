package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.OrderDeliveryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private UserDto userDto;

    private LocalDateTime localDateTime;

    private BigDecimal totalAmount;

    private List<OrderItemDto> orderItems;

    private OrderDeliveryData deliveryData;
}
