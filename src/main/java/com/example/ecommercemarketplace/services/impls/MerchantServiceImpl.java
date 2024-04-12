package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.exceptions.MerchantAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.MerchantNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.repositories.MerchantRepository;
import com.example.ecommercemarketplace.services.EmailConfirmationTokenService;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.utils.PublicIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final Mapper<Merchant, MerchantDto> merchantMapper;
    private final PublicIdGenerator publicIdGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    public MerchantDto findByEmail(String email) {
        Merchant merchant = merchantRepository.findByEmail(email).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with email=" + email +" is not found"));

        return merchantMapper.mapTo(merchant);
    }

    @Override
    public boolean existsByEmail(String email) {
        return merchantRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailConfirmationToken(String token) {
        return merchantRepository.existsByToken(token);
    }

    @Override
    public MerchantDto createMerchant(MerchantDto merchantDto) {
        if (merchantRepository.existsByEmail(merchantDto.getEmail())){
            throw new MerchantAlreadyExistsException("Merchant with email="+merchantDto.getEmail()+ " already exists");
        }

        String publicId = publicIdGenerator.generate();
        String hashedPassword = passwordEncoder.encode(merchantDto.getPassword());

        merchantDto.setPublicId(publicId);
        merchantDto.setPassword(hashedPassword);
        merchantDto.setStatus(MerchantStatus.ACTIVE);

        Merchant savedMerchant = merchantRepository.save(merchantMapper.mapFrom(merchantDto));

        return merchantMapper.mapTo(savedMerchant);
    }

    @Override
    public MerchantDto findByEmailConfirmationToken(String token) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findByToken(token);
        Merchant merchant = merchantRepository.findByEmailConfirmationToken(emailConfirmationToken).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with confirmationToken=" + token + " is not found"));

        return merchantMapper.mapTo(merchant);
    }

    @Override
    public MerchantDto updateMerchant(MerchantDto merchantDto) {
        Merchant updatedMerchant = merchantRepository.save(merchantMapper.mapFrom(merchantDto));

        return merchantMapper.mapTo(updatedMerchant);
    }

    @Override
    public MerchantDto findMerchantByPublicId(String publicId) {
        Merchant merchant = merchantRepository.findByPublicId(publicId).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId)));

        return merchantMapper.mapTo(merchant);
    }

    @Override
    public Page<MerchantDto> findAllMerchants(Pageable pageable) {
        Page<MerchantDto> merchants = merchantRepository.findAll(pageable).map(merchantMapper::mapTo);

        return merchants;
    }

    @Override
    public MerchantDto updateMerchantFully(String publicId, MerchantDto merchantDto) {
        Merchant merchant = merchantRepository.findByPublicId(publicId).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId)));

        merchant.setFirstName(merchantDto.getFirstName());
        merchant.setLastName((merchantDto.getLastName()));
        merchant.setPhoneNumber(merchantDto.getPhoneNumber());
        merchant.setWebsiteUrl(merchantDto.getWebsiteUrl());
        merchant.setType(merchantDto.getType());

        Merchant savedMerchant = merchantRepository.save(merchant);

        return merchantMapper.mapTo(savedMerchant);

    }

    @Override
    public MerchantDto updateMerchantPatch(String publicId, MerchantDto merchantDto) {
        if (!merchantRepository.existsByPublicId(publicId)) {
            throw new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId));
        }

        Merchant updatedMerchant = merchantRepository.findByPublicId(publicId).map(merchant -> {
            Optional.ofNullable(merchantDto.getFirstName()).ifPresent(merchant::setFirstName);
            Optional.ofNullable(merchantDto.getLastName()).ifPresent(merchant::setLastName);
            Optional.ofNullable(merchantDto.getPhoneNumber()).ifPresent(merchant::setPhoneNumber);
            return merchant;
        }).get();

        Merchant savedMerchant = merchantRepository.save(updatedMerchant);

        return merchantMapper.mapTo(savedMerchant);
    }

    @Override
    public void removeMerchantByPublicId(String publicId) {
        if (!merchantRepository.existsByPublicId(publicId)) {
            throw new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId));
        }

        merchantRepository.deleteByPublicId(publicId);
    }
}