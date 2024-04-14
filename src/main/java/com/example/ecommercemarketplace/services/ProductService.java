package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDto> findPageOfProductsByMerchant(String publicId, Pageable pageable);

    Page<ProductDto> findPageOfProductByCategory(Long categoryId, Pageable pageable);

    Page<ProductDto> findAll(Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto createProductWithMerchantId(String merchantPublicId, ProductDto productDto);

    void deleteProduct(Long id);

    void deleteProductWithMerchantId(String merchantPublicId, Long productId);

    ProductDto updateProductFully(Long productId, ProductDto productDto);

    ProductDto updateProductFullyWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto);

    ProductDto updateProductPatch(Long productId, ProductDto productDto);

    ProductDto updateProductPatchWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto findById(Long id);

    ProductDto findByIdWithMerchantId(String merchantPublicId, Long productId);

    void throwIfProductNotFound(Long productId);
}
