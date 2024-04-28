package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.exceptions.ProductNotFoundException;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.impls.ShoppingCartMapper;
import com.example.ecommercemarketplace.models.CartItem;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.models.ShoppingCart;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.CartItemRepository;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.repositories.ShoppingCartRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.ShoppingCartService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private ShoppingCart createShoppingCart(String email) {
        UserEntity user = getUserByEmail(email);
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        return shoppingCartRepository.save(cart);
    }

    private UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
    }

    @Override
    public ShoppingCartResponseDto addItemToShoppingCart(Authentication authentication, CartItemRequestDto cartItem) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with id=" + cartItem.getProductId() + " is not found"));

        String email = authentication.getName();
        UserEntity user = getUserByEmail(email);

        ShoppingCart shoppingCart;
        if (!shoppingCartRepository.existsByUser(user)) {
            shoppingCart = createShoppingCart(email);
            shoppingCart.setCartItems(new ArrayList<>());
        } else {
            shoppingCart = user.getShoppingCart();
        }

        CartItem cartItemEntity = CartItem.builder()
                .product(product)
                .quantity(cartItem.getQuantity())
                .shoppingCart(shoppingCart)
                .build();

        shoppingCart.getCartItems().add(cartItemEntity);
        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.mapToResponseDto(shoppingCart);
    }

    @Override
    public Page<CartItemDto> findAllItemsByShoppingCart(ShoppingCartDto shoppingCart) {
        return null;
    }
}
