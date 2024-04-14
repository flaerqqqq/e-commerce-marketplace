package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper implements Mapper<Product, ProductDto> {

    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final MerchantService merchantService;
    private final Mapper<Merchant, MerchantDto> merchantMapper;
    private final Mapper<Category, CategoryDto> categoryMapper;

    @Override
    public Product mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, Product.class);
    }

    @Override
    public ProductDto mapTo(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public ProductResponseDto toResponseDto(ProductDto productDto){
        ProductResponseDto responseDto = modelMapper.map(productDto, ProductResponseDto.class);
        responseDto.setCategoryId(productDto.getCategory().getId());
        responseDto.setMerchantId(productDto.getMerchant().getId());

        return responseDto;
    }

    public ProductDto requestToProductDto(ProductRequestDto productRequestDto){
        ProductDto productDto = modelMapper.map(productRequestDto, ProductDto.class);
        Category category = categoryMapper.mapFrom(categoryService.findById(productRequestDto.getCategoryId()));
        productDto.setCategory(category);

        return productDto;
    }

    public ProductDto updateRequestToProductDto(String merchantId, Long productId, ProductUpdateRequestDto productUpdateRequestDto){
        ProductDto productDto = modelMapper.map(productUpdateRequestDto, ProductDto.class);
        Category category = categoryMapper.mapFrom(categoryService.findById(productUpdateRequestDto.getCategoryId()));
        Merchant merchant = merchantMapper.mapFrom(merchantService.findMerchantByPublicId(merchantId));
        productDto.setCategory(category);
        productDto.setMerchant(merchant);
        productDto.setId(productId);

        return productDto;
    }

    public ProductDto patchUpdateRequestToProductDto(String merchantId, Long productId, ProductPatchUpdateRequestDto productPatchUpdateRequestDto){
        ProductDto productDto = modelMapper.map(productPatchUpdateRequestDto, ProductDto.class);

        if(productPatchUpdateRequestDto.getCategoryId() != null) {
            Category category = categoryMapper.mapFrom(categoryService.findById(productPatchUpdateRequestDto.getCategoryId()));
            productDto.setCategory(category);
        }

        Merchant merchant = merchantMapper.mapFrom(merchantService.findMerchantByPublicId(merchantId));
        productDto.setMerchant(merchant);
        productDto.setId(productId);

        return productDto;
    }
}
