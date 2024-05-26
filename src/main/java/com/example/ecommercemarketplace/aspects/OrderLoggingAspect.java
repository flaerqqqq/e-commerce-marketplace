package com.example.ecommercemarketplace.aspects;

import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class OrderLoggingAspect {

    private UserService userService;

    @After("execution(* com.example.ecommercemarketplace.controllers.OrderController.createOrder(..))" +
            "&& args(requestDto, authentication)")
    public void afterCreateOrder(OrderCreateRequestDto requestDto, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} created a new order with details: {}.", user.getPublicId(), requestDto);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.OrderController.getAllOrdersByUser(..))" +
            "&& args(authentication)")
    public void afterGetAllOrdersByUser(Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} retrieved all their orders.", user.getPublicId());
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.OrderController.getOrderById(..))" +
            "&& args(orderId, authentication)")
    public void afterGetOrderById(Long orderId, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} retrieved order with ID {}.", user.getPublicId(), orderId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.OrderController.getAllOrderItemsByOrderId(..))" +
            "&& args(orderId, authentication)")
    public void afterGetAllOrderItemsByOrderId(Long orderId, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} retrieved all items for order with ID {}.", user.getPublicId(), orderId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.OrderController.deleteOrderById(..))" +
            "&& args(orderId, authentication)")
    public void afterDeleteOrderById(Long orderId, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} deleted order with ID {}.", user.getPublicId(), orderId);
    }
}

