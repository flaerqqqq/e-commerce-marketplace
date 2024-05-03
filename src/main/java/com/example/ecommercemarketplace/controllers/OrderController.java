package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Transactional(Transactional.TxType.SUPPORTS)
    public OrderResponseDto createOrder(@RequestBody OrderCreateRequestDto requestDto,
                                        Authentication authentication) {
        return orderService.createOrder(requestDto, authentication);
    }


}
