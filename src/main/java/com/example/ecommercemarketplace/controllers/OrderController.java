package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Page<OrderResponseDto> getAllOrdersByUser(Pageable pageable,
                                                     Authentication authentication){
        return orderService.findAllOrdersByUser(pageable, authentication);
    }


}
