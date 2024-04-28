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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

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
        Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(() ->
                new ProductNotFoundException("Product with id=%d is not found".formatted(cartItem.getProductId())));

        CartItem cartItemEntity = CartItem.builder()
                .product(product)
                .quantity(cartItem.getQuantity())
                .build();

        String email = authentication.getName();
        UserEntity user = getUserByEmail(email);
        ShoppingCart cart = shoppingCartRepository.findByUser(user).orElseGet(() -> createShoppingCart(email));

        cartItemEntity.setShoppingCart(cart);

        cartItemRepository.save(cartItemEntity);

        return shoppingCartMapper.mapToResponseDto(shoppingCartRepository.findByUser(user).get());
    }

    @Override
    public Page<CartItemDto> findAllItemsByShoppingCart(ShoppingCartDto shoppingCart) {
        return null;
    }
}
