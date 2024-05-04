package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {

    OrderResponseDto createOrder(OrderCreateRequestDto requestDto, Authentication authentication);

    Page<OrderResponseDto> findAllOrdersByUser(Pageable pageable, Authentication authentication);
}
