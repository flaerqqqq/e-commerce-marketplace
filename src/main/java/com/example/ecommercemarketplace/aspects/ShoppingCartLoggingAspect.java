package com.example.ecommercemarketplace.aspects;

import com.example.ecommercemarketplace.dto.CartItemQuantityUpdateRequest;
import com.example.ecommercemarketplace.dto.CartItemRequestDto;
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
public class ShoppingCartLoggingAspect {

    private final UserService userService;

    @After("execution(* com.example.ecommercemarketplace.controllers.ShoppingCartController.addItemToShoppingCart(..)) && args(cartItemRequestDto, authentication)")
    public void afterAddItemToShoppingCart(CartItemRequestDto cartItemRequestDto, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} added item to shopping cart: {}", user.getPublicId(), cartItemRequestDto);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.ShoppingCartController.getAllItemsByCart(..)) && args(authentication)")
    public void afterGetAllItemsByCart(Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} retrieved all items from the shopping cart.", user.getPublicId());
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.ShoppingCartController.deleteCartItem(..)) && args(id, authentication)")
    public void afterDeleteCartItem(Long id, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} deleted item with id={} from the shopping cart.", user.getPublicId(), id);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.ShoppingCartController.updateCartItemQuantity(..)) && args(id, updateRequest, authentication)")
    public void afterUpdateCartItemQuantity(Long id, CartItemQuantityUpdateRequest updateRequest, Authentication authentication) {
        UserDto user = userService.findByEmail(authentication.getName());
        log.info("User with publicId={} updated quantity for item with id={} in the shopping cart: {}", user.getPublicId(), id, updateRequest.getQuantity());
    }
}
