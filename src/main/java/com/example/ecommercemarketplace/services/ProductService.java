package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Page<ProductDto> findPageOfProductsByMerchant(String publicId, Pageable pageable);

    Page<ProductDto> findPageOfProductByCategory(Long categoryId, Pageable pageable);

    Page<ProductResponseDto> findAll(Pageable pageable);

    ProductDto createProduct(ProductDto productDto, MultipartFile mainImage, List<MultipartFile> images);

    ProductDto createProductWithMerchantId(String merchantPublicId, ProductDto productDto, MultipartFile mainImage,
                                           List<MultipartFile> images);

    void deleteProduct(Long id);

    void deleteProductWithMerchantId(String merchantPublicId, Long productId);

    ProductDto updateProductFully(Long productId, ProductDto productDto);

    ProductDto updateProductFullyWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto);

    ProductDto updateProductPatch(Long productId, ProductDto productDto);

    ProductDto updateProductPatchWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto);

    ProductDto findById(Long id);

    ProductDto findByIdWithMerchantId(String merchantPublicId, Long productId);

    void throwIfProductNotFound(Long productId);

    Page<ProductResponseDto> searchProducts(String query, Pageable pageable);
}
