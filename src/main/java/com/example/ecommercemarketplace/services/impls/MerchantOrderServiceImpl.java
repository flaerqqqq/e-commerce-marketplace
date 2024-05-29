package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.MerchantOrderResponseDto;
import com.example.ecommercemarketplace.dto.StatusChangeRequestDto;
import com.example.ecommercemarketplace.exceptions.MerchantOrderNotFoundException;
import com.example.ecommercemarketplace.exceptions.MerchantOrderNotFoundInMerchantException;
import com.example.ecommercemarketplace.mappers.MerchantOrderMapper;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.MerchantOrder;
import com.example.ecommercemarketplace.repositories.MerchantOrderRepository;
import com.example.ecommercemarketplace.repositories.MerchantRepository;
import com.example.ecommercemarketplace.services.MerchantOrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MerchantOrderServiceImpl implements MerchantOrderService {

    private final MerchantOrderRepository merchantOrderRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantOrderMapper merchantOrderMapper;

    @Override
    public MerchantOrderResponseDto changeOrderStatus(Authentication authentication, Long orderId, StatusChangeRequestDto requestDto) {
        Merchant merchant = getMerchantByAuthentication(authentication);

        throwIfOrderIsNotFound(orderId);

        MerchantOrder merchantOrder = getOrThrowIfOrderNotFoundInMerchant(merchant, orderId);
        merchantOrder.setStatus(requestDto.getStatus());

        MerchantOrder updatedMerchantOrder = merchantOrderRepository.save(merchantOrder);

        return merchantOrderMapper.toResponseDto(updatedMerchantOrder);
    }

    @Override
    public Page<MerchantOrderResponseDto> findMerchantOrders(Authentication authentication, Pageable pageable) {
        return merchantOrderRepository.findAllByMerchant(getMerchantByAuthentication(authentication), pageable)
                .map(merchantOrderMapper::toResponseDto);
    }

    private Merchant getMerchantByAuthentication(Authentication authentication) {
        return merchantRepository.findByEmail(authentication.getName()).get();
    }

    private MerchantOrder getOrThrowIfOrderNotFoundInMerchant(Merchant merchant, Long orderId) {
        return merchant.getMerchantOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst().orElseThrow(() -> new MerchantOrderNotFoundInMerchantException("Order with id=%d is not found in merchant with id=%s order list".formatted(orderId, merchant.getPublicId())));
    }

    private void throwIfOrderIsNotFound(Long orderId) {
        if (!merchantOrderRepository.existsById(orderId)) {
            throw new MerchantOrderNotFoundException("Merchant order with id=%d is not found".formatted(orderId));
        }
    }
}
