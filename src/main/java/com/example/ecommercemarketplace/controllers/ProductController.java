package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.dto.ProductResponseDto;
import com.example.ecommercemarketplace.dto.ProductReviewRequestDto;
import com.example.ecommercemarketplace.dto.ProductReviewResponseDto;
import com.example.ecommercemarketplace.services.ProductReviewService;
import com.example.ecommercemarketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductReviewService productReviewService;

    @GetMapping
    public Page<ProductResponseDto> getAllProducts(Pageable pageable){
        return productService.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<ProductResponseDto> searchProductsByName(@RequestParam String query,
                                                         Pageable pageable){
        return productService.searchProducts(query, pageable);
    }

    @PostMapping("/{id}/product-reviews")
    @PreAuthorize("hasRole('USER')")
    public ProductReviewResponseDto createProductReview(@PathVariable("id") Long productId,
                                                        @RequestPart("text") @Valid ProductReviewRequestDto createRequest,
                                                        @RequestPart("media") List<MultipartFile> mediaContent,
                                                        Authentication authentication){
        return productReviewService.createProductReview(productId, createRequest, mediaContent, authentication);
    }
}
