package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderItemResponseDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.exceptions.AddressNotFoundException;
import com.example.ecommercemarketplace.exceptions.EmptyShoppingCartException;
import com.example.ecommercemarketplace.exceptions.OrderNotFoundException;
import com.example.ecommercemarketplace.exceptions.OrderNotFoundInUserException;
import com.example.ecommercemarketplace.mappers.OrderItemMapper;
import com.example.ecommercemarketplace.mappers.OrderMapper;
import com.example.ecommercemarketplace.models.*;
import com.example.ecommercemarketplace.models.enums.MerchantOrderStatus;
import com.example.ecommercemarketplace.models.enums.PaymentMethod;
import com.example.ecommercemarketplace.repositories.AddressRepository;
import com.example.ecommercemarketplace.repositories.OrderItemRepository;
import com.example.ecommercemarketplace.repositories.OrderRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.OrderService;
import com.example.ecommercemarketplace.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, Authentication authentication) {
        UserEntity user = getUserByAuthentication(authentication);

        throwIfShoppingCartEmpty(user);

        List<MerchantOrder> merchantOrders = getMerchantOrdersByCartItems(user.getShoppingCart().getCartItems());
        BigDecimal totalAmount = calculateTotalAmountByMerchantOrders(merchantOrders);
        OrderDeliveryData orderDeliveryData = getOrderDeliveryData(requestDto, authentication.getName());

        shoppingCartService.clearShoppingCart(authentication);

        Order order = buildOrder(totalAmount, orderDeliveryData, user, merchantOrders);

        setPaymentToOrder(order, requestDto.getPaymentMethod(), totalAmount);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public Page<OrderResponseDto> findAllOrdersByUser(Pageable pageable, Authentication authentication) {
        return orderRepository.findAllByUser(getUserByAuthentication(authentication), pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    public Page<OrderItemResponseDto> findAllOrderItemsByOrderId(Long orderId, Pageable pageable, Authentication authentication) {
        Order order = getOrderByIdOrThrow(orderId);

        throwIfUserNotHaveThatOrder(orderId, authentication);

        return orderItemRepository.findAllByMerchantOrderIn(order.getMerchantOrders(), pageable).map(orderItemMapper::toResponseDto);
    }

    @Override
    public void deleteOrderById(Long orderId, Authentication authentication) {
        throwIfUserNotHaveThatOrder(orderId, authentication);
        Order order = getOrderByIdOrThrow(orderId);
        orderRepository.delete(order);
    }

    @Override
    public OrderResponseDto findOrderById(Long orderId, Authentication authentication) {
        throwIfUserNotHaveThatOrder(orderId, authentication);
        Order order = getOrderByIdOrThrow(orderId);

        return orderMapper.toResponseDto(order);
    }

    private void throwIfShoppingCartEmpty(UserEntity user) {
        if (user.getShoppingCart().getCartItems().isEmpty()) {
            throw new EmptyShoppingCartException("Can't create order for user with email=%s because cart is empty".formatted(user.getEmail()));
        }
    }

    private Order getOrderByIdOrThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException("Order with id=%d is not found".formatted(orderId)));
    }

    private void throwIfUserNotHaveThatOrder(Long orderId, Authentication authentication) {
        UserEntity user = getUserByAuthentication(authentication);
        user.getOrders().stream()
                .map(Order::getId)
                .filter(id -> id.equals(orderId))
                .findFirst().orElseThrow(() ->
                        new OrderNotFoundInUserException("Order with id=%d is not created by user with email=%s".formatted(orderId, user.getEmail())));
    }

    private UserEntity getUserByAuthentication(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).get();
    }

    private Order buildOrder(BigDecimal totalAmount, OrderDeliveryData orderDeliveryData,
                             UserEntity user, List<MerchantOrder> merchantOrders) {
        Order order = Order.builder()
                .totalAmount(totalAmount)
                .orderTime(LocalDateTime.now())
                .user(user)
                .deliveryData(orderDeliveryData)
                .build();

        merchantOrders.forEach(mOrder -> mOrder.setParentOrder(order));
        order.setMerchantOrders(merchantOrders);

        return order;
    }

    private List<OrderItem> mapCartItemsToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem ->
                        OrderItem.builder()
                                .product(cartItem.getProduct())
                                .totalPrice(BigDecimal.valueOf(cartItem.getQuantity()).multiply(cartItem.getProduct().getPrice()))
                                .quantity(cartItem.getQuantity())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalAmountByMerchantOrders(List<MerchantOrder> merchantOrders) {
        return merchantOrders.stream()
                .map(MerchantOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<MerchantOrder> getMerchantOrdersByCartItems(List<CartItem> cartItems) {
        Map<Merchant, List<OrderItem>> orderItemsByMerchant = mapCartItemsToOrderItems(cartItems).stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getProduct().getMerchant(), Collectors.toList()));

        return orderItemsByMerchant.entrySet().stream()
                .map(entry -> buildMerchantOrderByOrderItems(entry.getKey(), entry.getValue()))
                .peek(order -> order.setStatus(MerchantOrderStatus.PENDING))
                .collect(Collectors.toList());
    }

    private MerchantOrder buildMerchantOrderByOrderItems(Merchant merchant, List<OrderItem> orderItems) {
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        MerchantOrder merchantOrder = MerchantOrder.builder()
                .merchant(merchant)
                .totalAmount(totalAmount)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(mOrder -> mOrder.setMerchantOrder(merchantOrder));

        return merchantOrder;
    }

    private void setPaymentToOrder(Order order, PaymentMethod paymentMethod, BigDecimal totalAmount) {
        if (paymentMethod != PaymentMethod.CASH_ON_DELIVERY) {
            Payment payment = Payment.builder()
                    .paymentTime(LocalDateTime.now())
                    .amount(totalAmount)
                    .method(paymentMethod)
                    .build();
            order.setPayment(payment);
        }
    }

    private OrderDeliveryData getOrderDeliveryData(OrderCreateRequestDto requestDto, String email) {
        Long addressId = requestDto.getDeliveryData().getAddressId();
        Address address = addressRepository.findById(addressId).orElseThrow(() ->
                new AddressNotFoundException("Address with id=%d for user with email=%s is not found".formatted(addressId, email)));

        return OrderDeliveryData.builder()
                .address(address)
                .method(requestDto.getDeliveryData().getMethod())
                .build();
    }
}
