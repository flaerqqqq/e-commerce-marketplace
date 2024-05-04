package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.OrderCreateRequestDto;
import com.example.ecommercemarketplace.dto.OrderResponseDto;
import com.example.ecommercemarketplace.exceptions.AddressNotFoundException;
import com.example.ecommercemarketplace.mappers.OrderMapper;
import com.example.ecommercemarketplace.models.*;
import com.example.ecommercemarketplace.models.enums.OrderStatus;
import com.example.ecommercemarketplace.models.enums.PaymentMethod;
import com.example.ecommercemarketplace.repositories.AddressRepository;
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
    private final ModelMapper modelMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto, Authentication authentication) {
        UserEntity user = getUserByAuthentication(authentication);
        List<MerchantOrder> merchantOrders = getMerchantOrdersByCartItems(user.getShoppingCart().getCartItems());
        BigDecimal totalAmount = calculateTotalAmountByMerchantOrders(merchantOrders);
        OrderDeliveryData orderDeliveryData = getOrderDeliveryData(requestDto, authentication.getName());

        shoppingCartService.clearShoppingCart(authentication);

        Order order = buildOrder(totalAmount, orderDeliveryData, user, merchantOrders);

        setPaymentToOrder(order, requestDto.getPaymentMethod(), totalAmount);

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }

    @Override
    public Page<OrderResponseDto> findAllOrdersByUser(Pageable pageable, Authentication authentication) {
        return orderRepository.findAllByUser(getUserByAuthentication(authentication), pageable)
                .map(orderMapper::toResponseDto);
    }

    private UserEntity getUserByAuthentication(Authentication authentication){
        return userRepository.findByEmail(authentication.getName()).get();
    }


    private Order buildOrder(BigDecimal totalAmount, OrderDeliveryData orderDeliveryData,
                             UserEntity user, List<MerchantOrder> merchantOrders) {
        Order order = Order.builder()
                .totalAmount(totalAmount)
                .orderTime(LocalDateTime.now())
                .status(OrderStatus.PENDING)
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
