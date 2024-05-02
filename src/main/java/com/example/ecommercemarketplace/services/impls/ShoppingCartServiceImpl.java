package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.CartItemQuantityUpdateRequest;
import com.example.ecommercemarketplace.dto.CartItemRequestDto;
import com.example.ecommercemarketplace.dto.CartItemResponseDto;
import com.example.ecommercemarketplace.dto.ShoppingCartResponseDto;
import com.example.ecommercemarketplace.exceptions.*;
import com.example.ecommercemarketplace.mappers.impls.CartItemMapper;
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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final ModelMapper modelMapper;

    private ShoppingCart createShoppingCart(String email) {
        UserEntity user = getUserByEmail(email);
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        return shoppingCartRepository.save(cart);
    }

    private UserEntity getUserByEmail(String email) {
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
    public Page<CartItemResponseDto> getAllItemsByShoppingCart(Authentication authentication, Pageable pageable) {
        String email = authentication.getName();
        ShoppingCart cart = getShoppingCartByUser(email);

        return cartItemRepository.findByShoppingCart(cart, pageable).map(cartItemMapper::mapToResponseDto);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public void deleteCartItem(Authentication authentication, Long id) {
        CartItem cartItem = validateAndGetCartItem(authentication, id);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItemResponseDto updateCartItemQuantity(Authentication authentication, Long id, CartItemQuantityUpdateRequest updateRequest) {
        CartItem cartItem = validateAndGetCartItem(authentication, id);
        cartItem.setQuantity(updateRequest.getQuantity());
        CartItem updatedCart = cartItemRepository.save(cartItem);

        return  modelMapper.map(updatedCart, CartItemResponseDto.class);
    }

    private CartItem validateAndGetCartItem(Authentication authentication, Long id){
        if (!cartItemRepository.existsById(id)) {
            throw new CartItemNotFoundException("Cart item with id=%d is not found".formatted(id));
        }

        String email = authentication.getName();
        ShoppingCart cart = shoppingCartRepository.findByUser(getUserByEmail(email)).orElseThrow(() ->
                new ShoppingCartNotFoundException("Shopping cart for user with email=%s is not found".formatted(email)));

        return cart.getCartItems().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new CartItemNotFoundInCartException("Cart item with id=%d is not found in shopping cart with id=%d".formatted(id, cart.getId())));
    }


    private ShoppingCart getShoppingCartByUser(String email) {
        return shoppingCartRepository.findByUser(getUserByEmail(email)).orElseGet(() ->
                createShoppingCart(email));
    }

}
