package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.exceptions.MerchantNotFoundException;
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

import java.util.Optional;

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
    public ProductDto createProductWithMerchantId(String merchantPublicId, ProductDto productDto) {
        Merchant merchant = merchantMapper.mapFrom(merchantService.findMerchantByPublicId(merchantPublicId));

        productDto.setMerchant(merchant);

        return createProduct(productDto);
    }

    @Override
    public void deleteProduct(Long id) {
        throwIfProductNotFound(id);

        productRepository.deleteById(id);
    }

    @Override
    public void deleteProductWithMerchantId(String merchantPublicId, Long productId) {
        merchantService.throwIfMerchantNotFoundByPublicId(merchantPublicId);

        deleteProduct(productId);
    }


    @Override
    public ProductDto updateProductFully(Long productId, ProductDto productDto) {
        throwIfProductNotFound(productId);

        Product product = productMapper.mapFrom(productDto);
        product.setId(productId);

        Product updatedProduct = productRepository.save(product);

        return productMapper.mapTo(updatedProduct);
    }

    @Override
    public ProductDto updateProductFullyWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto) {
        merchantService.throwIfMerchantNotFoundByPublicId(merchantPublicId);

        return updateProductFully(productId, productDto);
    }

    @Override
    public ProductDto updateProductPatch(Long productId, ProductDto productDto) {
        throwIfProductNotFound(productId);

        Product product = productRepository.findById(productId).map(prod -> {
            Optional.ofNullable(productDto.getName()).ifPresent(prod::setName);
            Optional.ofNullable(productDto.getCategory()).ifPresent(prod::setCategory);
            Optional.ofNullable(productDto.getPrice()).ifPresent(prod::setPrice);
            Optional.ofNullable(productDto.getInventory()).ifPresent(prod::setInventory);
            Optional.ofNullable(productDto.getDescription()).ifPresent(prod::setDescription);
            return prod;
        }).get();
        Product savedProduct = productRepository.save(product);
        return productMapper.mapTo(savedProduct);
    }

    @Override
    public ProductDto updateProductPatchWithMerchantId(String merchantPublicId, Long productId, ProductDto productDto) {
        merchantService.throwIfMerchantNotFoundByPublicId(merchantPublicId);

        return updateProductPatch(productId, productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        throwIfProductNotFound(productDto.getId());

        Product savedProduct = productRepository.save(productMapper.mapFrom(productDto));

        return productMapper.mapTo(savedProduct);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id=%d is not found".formatted(id)));

        return productMapper.mapTo(product);
    }

    @Override
    public ProductDto findByIdWithMerchantId(String merchantPublicId, Long productId) {
        merchantService.throwIfMerchantNotFoundByPublicId(merchantPublicId);

        return findById(productId);
    }

    @Override
    public void throwIfProductNotFound(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product with id=%d is not found".formatted(productId));
        }
    }


}