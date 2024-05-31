package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.MerchantService;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        uses = {
                CategoryMapper.class,
                MerchantMapper.class,
                MainProductImageMapper.class,
                ProductImageMapper.class
        })
public abstract class ProductMapper {

    @Autowired
    protected CategoryMapper categoryMapper;
    @Autowired
    protected MerchantMapper merchantMapper;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected MerchantService merchantService;

    public abstract ProductDto  mapTo(Product product);

    public abstract Product mapFrom(ProductDto productDto);

    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "mainProductImage", target = "mainProductImage")
    public abstract ProductResponseDto toResponseDto(ProductDto productDto);

    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "category.id", target = "categoryId")
    public abstract ProductResponseDto mapProductToResponseDto(Product product);

    @Mapping(target = "category",
            expression = "java(productRequestDto.getCategoryId() != null ? categoryMapper.mapFrom(categoryService.findById(productRequestDto.getCategoryId())) : null)")
    public abstract ProductDto requestToProductDto(ProductRequestDto productRequestDto);

    @Mapping(source = "productId", target = "id")
    @Mapping(target = "category",
            expression = "java(updateRequest.getCategoryId() != null ? categoryMapper.mapFrom(categoryService.findById(updateRequest.getCategoryId())) : null)")
    public abstract ProductDto updateRequestToProductDto(String merchantId, Long productId, ProductUpdateRequestDto updateRequest);

    @Mapping(source = "productId", target = "id")
    @Mapping(target = "category",
            expression = "java(updateRequest.getCategoryId() != null ? categoryMapper.mapFrom(categoryService.findById(updateRequest.getCategoryId())) : null)")
    public abstract ProductDto patchUpdateRequestToProductDto(String merchantId, Long productId,
                                                              ProductPatchUpdateRequestDto updateRequest);
}
