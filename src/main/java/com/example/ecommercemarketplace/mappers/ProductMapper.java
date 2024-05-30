package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.mappers.impls.CategoryMapper;
import com.example.ecommercemarketplace.mappers.impls.MerchantMapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.MerchantService;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, MerchantMapper.class})
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "category.id", target = "categoryId")
    ProductResponseDto toResponseDto(ProductDto productDto);

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "fetchCategory")
    ProductDto requestToProductDto(ProductRequestDto productRequestDto);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "fetchCategory")
    @Mapping(source = "merchantId", target = "merchant", qualifiedByName = "fetchMerchant")
    ProductDto updateRequestToProductDto(String merchantId, Long productId, ProductUpdateRequestDto productUpdateRequestDto);


    @Mapping(source = "productId", target = "id")
    @Mapping(source = "categoryId", target = "category", qualifiedByName = "fetchCategory")
    @Mapping(source = "merchantId", target = "merchant", qualifiedByName = "fetchMerchant")
    ProductDto patchUpdateRequestToProductDto(String merchantId, Long productId, ProductPatchUpdateRequestDto productPatchUpdateRequestDto);

    @Named("fetchCategory")
    default Category fetchCategory(Long categoryId,
                                   @Context CategoryService categoryService,
                                   @Context CategoryMapper categoryMapper) {
        return categoryId != null ? categoryMapper.mapFrom(categoryService.findById(categoryId)) : null;
    }

    @Named("fetchMerchant")
    default Merchant fetchMerchant(String merchantId,
                                   @Context MerchantService merchantService,
                                   @Context MerchantMapper merchantMapper) {
        return merchantMapper.mapFrom(merchantService.findMerchantByPublicId(merchantId));
    }
}
