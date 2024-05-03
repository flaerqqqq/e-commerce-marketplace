package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import org.springframework.security.core.Authentication;

public interface OrderService {

    OrderResponseDto createOrder(OrderCreateRequestDto requestDto, Authentication authentication);
}
