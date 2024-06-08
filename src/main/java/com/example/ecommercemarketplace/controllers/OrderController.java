package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderItemResponseDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Transactional(Transactional.TxType.SUPPORTS)
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createOrder(@RequestBody OrderCreateRequestDto requestDto, Authentication authentication) {
        return orderService.createOrder(requestDto, authentication);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> getAllOrdersByUser(Pageable pageable, Authentication authentication) {
        return orderService.findAllOrdersByUser(pageable, authentication);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto getOrderById(@PathVariable("id") Long orderId, Authentication authentication) {
        return orderService.findOrderById(orderId, authentication);
    }

    @GetMapping("/{id}/order-items")
    @PreAuthorize("hasRole('USER')")
    public Page<OrderItemResponseDto> getAllOrderItemsByOrderId(@PathVariable("id") Long orderId,
                                                                Pageable pageable,
                                                                Authentication authentication) {
        return orderService.findAllOrderItemsByOrderId(orderId, pageable, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional(Transactional.TxType.SUPPORTS)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteOrderById(@PathVariable("id") Long orderId, Authentication authentication) {
        orderService.deleteOrderById(orderId, authentication);
    }
}
