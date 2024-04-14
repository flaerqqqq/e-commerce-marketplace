package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDto> findPageOfProductsByMerchant(String publicId, Pageable pageable);

    Page<ProductDto> findPageOfProductByCategory(Long categoryId, Pageable pageable);

    Page<ProductDto> findAll(Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(Long id);

    ProductDto updateProductFully(Long productId, ProductDto productDto);

    ProductDto updateProductPatch(Long productId, ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto findById(Long id);
}
