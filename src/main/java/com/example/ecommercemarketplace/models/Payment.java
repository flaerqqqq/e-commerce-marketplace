package com.example.ecommercemarketplace.models;

import com.example.ecommercemarketplace.models.Order;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.models.enums.DeliveryMethod;
import com.example.ecommercemarketplace.models.enums.OrderStatus;
import com.example.ecommercemarketplace.models.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private Long id;

    private Order order;

    private LocalDateTime paymentTime;

    private BigDecimal amount;

    private PaymentMethod method;
}









