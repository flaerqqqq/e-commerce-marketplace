package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.MerchantOrderResponseDto;
import com.example.ecommercemarketplace.dto.StatusChangeRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface MerchantOrderService {

    MerchantOrderResponseDto changeOrderStatus(Authentication authentication, Long orderId, StatusChangeRequestDto requestDto);

    Page<MerchantOrderResponseDto> findMerchantOrders(Authentication authentication, Pageable pageable);
}
