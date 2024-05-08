package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.MerchantOrderResponseDto;
import com.example.ecommercemarketplace.dto.StatusChangeRequestDto;
import com.example.ecommercemarketplace.services.MerchantOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant-orders")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
public class MerchantOrderController {

    private final MerchantOrderService merchantOrderService;

    @PatchMapping("/{id}")
    public MerchantOrderResponseDto changeMerchantOrderStatus(@RequestBody @Valid StatusChangeRequestDto requestDto,
                                                              @PathVariable Long id,
                                                              Authentication authentication) {
        return merchantOrderService.changeOrderStatus(authentication, id, requestDto);
    }

    @GetMapping
    public Page<MerchantOrderResponseDto> getMerchantOrdersByMerchant(Pageable pageable,
                                                                     Authentication authentication){
        return merchantOrderService.findMerchantOrders(authentication, pageable);
    }


}
