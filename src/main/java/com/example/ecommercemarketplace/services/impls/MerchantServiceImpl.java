package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.exceptions.MerchantAlreadyExistsException;
import com.example.ecommercemarketplace.exceptions.MerchantNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.UserEntity;
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
                new MerchantNotFoundException("Merchant with email=%s is not found".formatted(email)));
        return merchantMapper.mapTo(merchant);
    }

    @Override
    public boolean existsByEmail(String email) {
        return merchantRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPublicId(String publicId) {
        return merchantRepository.existsByPublicId(publicId);
    }

    @Override
    public boolean existsByEmailConfirmationToken(String token) {
        return merchantRepository.existsByToken(token);
    }

    @Override
    public boolean isMerchant(String email) {
        Optional<UserEntity> merchantOptional = merchantRepository.findByEmail(email).map(merchant -> merchant);
        return merchantOptional.isPresent() && merchantOptional.get() instanceof UserEntity;
    }

    @Override
    public MerchantDto createMerchant(MerchantDto merchantDto) {
        if (merchantRepository.existsByEmail(merchantDto.getEmail())) {
            throw new MerchantAlreadyExistsException("Merchant with email=%s already exists".formatted(merchantDto.getEmail()));
        }

        String publicId = publicIdGenerator.generate();
        String hashedPassword = passwordEncoder.encode(merchantDto.getPassword());

        merchantDto.setPublicId(publicId);
        merchantDto.setPassword(hashedPassword);
        merchantDto.setStatus(MerchantStatus.ACTIVE);
        merchantDto.setLoginData(new LoginData());

        Merchant savedMerchant = merchantRepository.save(merchantMapper.mapFrom(merchantDto));

        return merchantMapper.mapTo(savedMerchant);
    }

    @Override
    public MerchantDto findByEmailConfirmationToken(String token) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findByToken(token);
        Merchant merchant = merchantRepository.findByEmailConfirmationToken(emailConfirmationToken).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with confirmationToken=%s is not found".formatted(token)));
        return merchantMapper.mapTo(merchant);
    }

    @Override
    public MerchantDto updateMerchant(MerchantDto merchantDto) {
        Merchant updatedMerchant = merchantRepository.save(merchantMapper.mapFrom(merchantDto));
        return merchantMapper.mapTo(updatedMerchant);
    }

    @Override
    public MerchantDto findMerchantByPublicId(String publicId) {
        Merchant merchant = findByPublicId(publicId);
        return merchantMapper.mapTo(merchant);
    }

    @Override
    public Page<MerchantDto> findAllMerchants(Pageable pageable) {
        return merchantRepository.findAll(pageable).map(merchantMapper::mapTo);
    }

    @Override
    public MerchantDto updateMerchantFully(String publicId, MerchantDto merchantDto) {
        Merchant merchant = findByPublicId(publicId);

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
        throwIfMerchantNotFoundByPublicId(publicId);

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
        throwIfMerchantNotFoundByPublicId(publicId);
        merchantRepository.deleteByPublicId(publicId);
    }

    @Override
    public void throwIfMerchantNotFoundByPublicId(String publicId) {
        if (!merchantRepository.existsByPublicId(publicId)) {
            throw new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId));
        }
    }

    private Merchant findByPublicId(String publicId){
        return merchantRepository.findByPublicId(publicId).orElseThrow(() ->
                new MerchantNotFoundException("Merchant with publicId=%s is not found".formatted(publicId)));
    }
}
