package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.exceptions.ProductNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MerchantService merchantService;
    private final CategoryService categoryService;
    private final Mapper<Merchant, MerchantDto> merchantMapper;
    private final Mapper<Product, ProductDto> productMapper;
    private final Mapper<Category, CategoryDto> categoryMapper;

    @Override
    public Page<ProductDto> findPageOfProductsByMerchant(String publicId, Pageable pageable) {
        Merchant merchant = merchantMapper.mapFrom(merchantService.findMerchantByPublicId(publicId));

        Page<ProductDto> pageOfProducts = productRepository.findByMerchant(merchant, pageable).map(productMapper::mapTo);

        return pageOfProducts;
    }

    @Override
    public Page<ProductDto> findPageOfProductByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryMapper.mapFrom(categoryService.findById(categoryId));

        Page<ProductDto> pageOfProducts = productRepository.findByCategory(category, pageable).map(productMapper::mapTo);

        return pageOfProducts;
    }

    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<ProductDto> pageOfUsers = productRepository.findAll(pageable).map(productMapper::mapTo);

        return pageOfUsers;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.mapFrom(productDto);

        Product savedProduct = productRepository.save(product);

        return productMapper.mapTo(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)){
            throw new ProductNotFoundException("Product with id=%d is not found".formatted(id));
        }
        productRepository.deleteById(id);
    }


    @Override
    public ProductDto updateProductFully(Long productId, ProductDto productDto) {
        if (!productRepository.existsById(productId)){
            throw new ProductNotFoundException("Product with id=%d is not found".formatted(productId));
        }

        Product product = productMapper.mapFrom(productDto);
        product.setId(productId);

        Product updatedProduct = productRepository.save(product);

        return productMapper.mapTo(updatedProduct);
    }

    @Override
    public ProductDto updateProductPatch(Long productId, ProductDto productDto) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(productDto.getId())) {
            throw new ProductNotFoundException("Product with id=%d is not found".formatted(productDto.getId()));
        }

        Product savedProduct = productRepository.save(productMapper.mapFrom(productDto));

        return productMapper.mapTo(savedProduct);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id=%d is not found".formatted(id)));

        return productMapper.mapTo(product);
    }


}
