package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.dto.ProductResponseDto;
import com.example.ecommercemarketplace.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> getAllProducts(Pageable pageable){
        return productService.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<ProductResponseDto> searchProductsByName(@RequestParam String query,
                                                         Pageable pageable){
        return productService.searchProducts(query, pageable);
    }
}
