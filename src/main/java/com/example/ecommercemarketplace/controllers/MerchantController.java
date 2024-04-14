package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.mappers.impls.ProductMapper;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
@AllArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public MerchantResponseDto getMerchantByMerchantPublicId(@PathVariable("id") String id) {
        MerchantDto merchant = merchantService.findMerchantByPublicId(id);
        MerchantResponseDto merchantResponseDto = new MerchantResponseDto();

        BeanUtils.copyProperties(merchant, merchantResponseDto);

        return merchantResponseDto;
    }

    @GetMapping
    public Page<MerchantResponseDto> getAllMerchants(Pageable pageable) {
        Page<MerchantResponseDto> page = merchantService.findAllMerchants(pageable).map(merchantDto ->
        {
            MerchantResponseDto merchantResponseDto = new MerchantResponseDto();
            BeanUtils.copyProperties(merchantDto, merchantResponseDto);
            return merchantResponseDto;
        });

        return page;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MerchantUpdateResponseDto updateMerchantFully(@RequestBody @Valid MerchantUpdateRequestDto merchantUpdateRequestDto,
                                                         @PathVariable("id") String id) {
        MerchantDto merchantDto = new MerchantDto();
        BeanUtils.copyProperties(merchantUpdateRequestDto, merchantDto);

        MerchantDto updatedMerchant = merchantService.updateMerchantFully(id, merchantDto);

        MerchantUpdateResponseDto merchantUpdateResponseDto = new MerchantUpdateResponseDto();
        BeanUtils.copyProperties(updatedMerchant, merchantUpdateResponseDto);

        return merchantUpdateResponseDto;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MerchantUpdateResponseDto updateMerchantPatch(@RequestBody @Valid MerchantPatchUpdateRequestDto merchantPatchUpdateRequestDto,
                                                         @PathVariable("id") String id) {
        MerchantDto merchantDto = new MerchantDto();
        BeanUtils.copyProperties(merchantPatchUpdateRequestDto, merchantDto);

        MerchantDto updatedMerchant = merchantService.updateMerchantPatch(id, merchantDto);

        MerchantUpdateResponseDto merchantUpdateResponseDto = new MerchantUpdateResponseDto();
        BeanUtils.copyProperties(updatedMerchant, merchantUpdateResponseDto);

        return merchantUpdateResponseDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMerchant(@PathVariable("id") String id) {
        merchantService.removeMerchantByPublicId(id);
    }

    @GetMapping("/{id}/products")
    public Page<ProductResponseDto> getProductsByMerchant(@PathVariable("id") String publicId, Pageable pageable){
        Page<ProductResponseDto> pageOfProducts = productService.findPageOfProductsByMerchant(publicId, pageable)
                .map(productMapper::toResponseDto);

        return pageOfProducts;
    }

    @GetMapping("/{id}/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable("id") String publicId,
                                             @PathVariable("productId") Long productId){
        ProductResponseDto productResponseDto = productMapper.toResponseDto(productService.findByIdWithMerchantId(publicId, productId));

        return productResponseDto;
    }

    @PostMapping("/{id}/products")
    public ProductResponseDto createProduct(@PathVariable("id") String publicId,
                                            @RequestBody ProductRequestDto productRequestDto){
        ProductDto productDto = productMapper.requestToProductDto(productRequestDto);

        ProductDto createdUser = productService.createProductWithMerchantId(publicId, productDto);

        return productMapper.toResponseDto(createdUser);
    }

    @PutMapping("/{id}/products/{productId}")
    public ProductResponseDto updateProductFully(@PathVariable("id") String publicId,
                                                 @PathVariable("productId") Long productId,
                                                 @RequestBody ProductUpdateRequestDto productUpdateRequestDto){
        ProductDto updatedProduct = productService.updateProductFullyWithMerchantId(
                publicId,
                productId,
                productMapper.updateRequestToProductDto(publicId, productId, productUpdateRequestDto)
        );

        return productMapper.toResponseDto(updatedProduct);
    }

    @PatchMapping("/{id}/products/{productId}")
    public ProductResponseDto updateProductPatch(@PathVariable("id") String publicId,
                                                 @PathVariable("productId") Long productId,
                                                 @RequestBody ProductPatchUpdateRequestDto productPatchUpdateRequestDto){
        ProductDto updatedProduct = productService.updateProductPatchWithMerchantId(
                publicId,
                productId,
                productMapper.patchUpdateRequestToProductDto(publicId, productId, productPatchUpdateRequestDto)
        );

        return productMapper.toResponseDto(updatedProduct);
    }

    @DeleteMapping("/{id}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") String publicId,
                              @PathVariable("productId") Long productId){
        productService.deleteProductWithMerchantId(publicId, productId);
    }

}
