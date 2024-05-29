package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long id;

    private LocalDateTime paymentTime;

    private BigDecimal totalAmount;

    private PaymentMethod method;
}
