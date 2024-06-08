package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.mappers.MerchantMapper;
import com.example.ecommercemarketplace.mappers.ProductMapper;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
@AllArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ModelMapper modelMapper;
    private final MerchantMapper merchantMapper;

    @GetMapping("/{id}")
    public MerchantResponseDto findByPublicId(@PathVariable("id") String publicId) {
        MerchantDto merchant = merchantService.findMerchantByPublicId(publicId);
        return merchantMapper.toResponseDto(merchant);
    }

    @GetMapping
    public Page<MerchantResponseDto> findAllMerchants(Pageable pageable) {
        return merchantService.findAllMerchants(pageable)
                .map(merchantMapper::toResponseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public MerchantUpdateResponseDto updateMerchantFully(@RequestBody @Valid MerchantUpdateRequestDto updateRequest,
                                                         @PathVariable("id") String publicId) {
        MerchantDto merchantDto = modelMapper.map(updateRequest, MerchantDto.class);
        MerchantDto updatedMerchant = merchantService.updateMerchantFully(publicId, merchantDto);

        return modelMapper.map(updatedMerchant, MerchantUpdateResponseDto.class);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public MerchantUpdateResponseDto updateMerchantPatch(@RequestBody @Valid MerchantPatchUpdateRequestDto updateRequest,
                                                         @PathVariable("id") String publicId) {
        MerchantDto merchantDto = modelMapper.map(updateRequest, MerchantDto.class);
        MerchantDto updatedMerchant = merchantService.updateMerchantPatch(publicId, merchantDto);

        return modelMapper.map(updatedMerchant, MerchantUpdateResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public void deleteMerchant(@PathVariable("id") String publicId) {
        merchantService.removeMerchantByPublicId(publicId);
    }

    @GetMapping("/{id}/products")
    public Page<ProductResponseDto> getProductsByMerchant(@PathVariable("id") String publicId, Pageable pageable) {
        return productService.findPageOfProductsByMerchant(publicId, pageable)
                .map(productMapper::toResponseDto);
    }

    @GetMapping("/{id}/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable("id") String publicId,
                                             @PathVariable("productId") Long productId) {
        return productMapper.toResponseDto(productService.findByIdWithMerchantId(publicId, productId));
    }

    @PostMapping(value = "/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MERCHANT')")
    public ProductResponseDto createProduct(@PathVariable("id") String publicId,
                                            @RequestPart("product") @Valid ProductRequestDto productRequest,
                                            @RequestPart("mainImage") MultipartFile mainImage,
                                            @RequestPart("images") List<MultipartFile> images) {
        ProductDto productDto = productMapper.requestToProductDto(productRequest);
        ProductDto createdUser = productService.createProductWithMerchantId(publicId, productDto, mainImage, images);

        return productMapper.toResponseDto(createdUser);
    }

    @PutMapping("/{id}/products/{productId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public ProductResponseDto updateProductFully(@PathVariable("id") String publicId,
                                                 @PathVariable("productId") Long productId,
                                                 @RequestBody ProductUpdateRequestDto updateRequest) {
        ProductDto updatedProduct = productService.updateProductFullyWithMerchantId(
                publicId,
                productId,
                productMapper.updateRequestToProductDto(publicId, productId, updateRequest)
        );
        return productMapper.toResponseDto(updatedProduct);
    }

    @PatchMapping("/{id}/products/{productId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public ProductResponseDto updateProductPatch(@PathVariable("id") String publicId,
                                                 @PathVariable("productId") Long productId,
                                                 @RequestBody ProductPatchUpdateRequestDto updateRequest) {
        ProductDto updatedProduct = productService.updateProductPatchWithMerchantId(
                publicId,
                productId,
                productMapper.patchUpdateRequestToProductDto(publicId, productId, updateRequest)
        );
        return productMapper.toResponseDto(updatedProduct);
    }

    @DeleteMapping("/{id}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public void deleteProduct(@PathVariable("id") String publicId,
                              @PathVariable("productId") Long productId) {
        productService.deleteProductWithMerchantId(publicId, productId);
    }
}
