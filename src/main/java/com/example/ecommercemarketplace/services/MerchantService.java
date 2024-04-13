package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.MerchantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchantService {

    MerchantDto findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailConfirmationToken(String token);

    boolean isMerchant(String email);

    MerchantDto createMerchant(MerchantDto merchantDto);

    MerchantDto findByEmailConfirmationToken(String token);

    MerchantDto updateMerchant(MerchantDto merchantDto);

    MerchantDto findMerchantByPublicId(String publicId);

    Page<MerchantDto> findAllMerchants(Pageable pageable);

    MerchantDto updateMerchantFully(String publicId, MerchantDto merchantDto);

    MerchantDto updateMerchantPatch(String publicId, MerchantDto merchantDto);

    void removeMerchantByPublicId(String publicId);
}
