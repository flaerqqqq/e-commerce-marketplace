package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.ProductDto;
import com.example.ecommercemarketplace.dto.ProductResponseDto;
import com.example.ecommercemarketplace.exceptions.MerchantNotFoundException;
import com.example.ecommercemarketplace.exceptions.ProductNotFoundException;
import com.example.ecommercemarketplace.mappers.CategoryMapper;
import com.example.ecommercemarketplace.mappers.MerchantMapper;
import com.example.ecommercemarketplace.mappers.ProductMapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.Product;
import com.example.ecommercemarketplace.repositories.ProductRepository;
import com.example.ecommercemarketplace.services.impls.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MerchantService merchantService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @InjectMocks
    private ProductServiceImpl productService;

    private final String merchantPublicId = "publicId";
    private final Long productId = 1L;
    private final Long categoryId = 1L;
    private Merchant merchant;
    private MerchantDto merchantDto;
    private Category category;
    private Product product;
    private ProductResponseDto productResponseDto;
    private ProductDto productDto;

    @BeforeEach
    public void setUp(){
        category = Category.builder()
                .id(categoryId)
                .categoryName("test")
                .description("test")
                .products(Collections.singletonList(product))
                .build();
        product = Product.builder()
                .id(productId)
                .name("product")
                .category(category)
                .description("desc")
                .inventory(10L)
                .price(BigDecimal.ONE)
                .merchant(merchant)
                .build();
        merchant = Merchant.builder()
                .publicId(merchantPublicId)
                .email("test@gmail.com")
                .phoneNumber("+3801111111111")
                .password("password1234")
                .firstName("name")
                .lastName("surname")
                .isEnabled(true)
                .websiteUrl("test.com")
                .registrationDate(LocalDateTime.now())
                .products(Collections.singletonList(product))
                .build();
        merchantDto = MerchantDto.builder()
                .publicId(merchantPublicId)
                .email("test@gmail.com")
                .phoneNumber("+3801111111111")
                .password("password1234")
                .firstName("name")
                .lastName("surname")
                .isEnabled(true)
                .websiteUrl("test.com")
                .registrationDate(LocalDateTime.now())
                .products(Collections.singletonList(product))
                .build();
        productDto = ProductDto.builder()
                .id(productId)
                .name("product")
                .category(category)
                .description("desc")
                .inventory(10L)
                .price(BigDecimal.ONE)
                .merchant(merchant)
                .build();
        productResponseDto = ProductResponseDto.builder()
                .id(productId)
                .name("product")
                .categoryId(categoryId)
                .description("desc")
                .inventory(10L)
                .price(BigDecimal.ONE)
                .merchantId(merchant.getId())
                .build();
    }

    @AfterEach
    public void cleanUp(){
        merchant = null;
        merchantDto = null;
        category = null;
        product = null;
        productResponseDto = null;
        productDto = null;
    }

    @Test
    public void findAll_shouldReturnPageOfProducts(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> pageOfProducts = new PageImpl<>(List.of(product));
        Page<ProductResponseDto> expectedPageOfProductsDto = new PageImpl<>(List.of(productResponseDto));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(pageOfProducts);
        when(productMapper.mapProductToResponseDto(any(Product.class))).thenReturn(productResponseDto);

        Page<ProductResponseDto> actualPageOfProducts = productService.findAll(pageable);

        assertThat(actualPageOfProducts.getContent()).isEqualTo(expectedPageOfProductsDto.getContent());
    }

    @Test
    public void createProduct_shouldCreateProduct(){
        when(productMapper.mapFrom(any(ProductDto.class))).thenReturn(product);
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto actualProductDto = productService.createProduct(productDto);

        assertThat(actualProductDto).isEqualTo(productDto);
    }

    @Test
    public void createProductWithMerchantId_shouldCreateProduct_whenMerchantFound(){
        when(merchantMapper.mapFrom(any(MerchantDto.class))).thenReturn(merchant);
        when(merchantService.findMerchantByPublicId(anyString())).thenReturn(merchantDto);
        when(productMapper.mapFrom(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);

        ProductDto actualProductDto = productService.createProductWithMerchantId(merchantPublicId, productDto, null, null);

        assertThat(actualProductDto).isEqualTo(productDto);
    }

    @Test
    public void createProductWithMerchantId_shouldThrow_whenMerchantNotFound(){
        when(merchantService.findMerchantByPublicId(anyString())).thenThrow(MerchantNotFoundException.class);

        assertThrows(MerchantNotFoundException.class, () ->
                productService.createProductWithMerchantId(merchantPublicId, productDto, null, null));
    }

    @Test
    public void deleteProduct_shouldDeleteProduct_whenProductFound(){
        when(productRepository.existsById(anyLong())).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    public void deleteProduct_shouldThrow_whenProductNotFound(){
        assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProduct(productId));
    }


    @Test
    public void deleteProductWithMerchantId_shouldThrow_whenMerchantNotFound(){
        doThrow(MerchantNotFoundException.class).when(merchantService).throwIfMerchantNotFoundByPublicId(anyString());

        assertThrows(MerchantNotFoundException.class, () ->
                productService.deleteProductWithMerchantId(merchantPublicId, productId));
    }

    @Test
    public void deleteProductWithMerchantId_shouldThrow_whenProductNotFound(){
        assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProductWithMerchantId(merchantPublicId, productId));
    }

    @Test
    public void updateProductFully_shouldUpdateProduct_whenProductFound(){
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(productMapper.mapFrom(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);

        ProductDto actualUpdatedProductDto = productService.updateProductFully(productId, productDto);

        assertThat(actualUpdatedProductDto).isEqualTo(productDto);
    }

    @Test
    public void updateProductFully_shouldThrow_whenProductNotFound(){
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProductFully(productId, productDto));
    }

    @Test
    public void updateProductFullyWithMerchantId_shouldThrow_whenMerchantNotFound(){
        doThrow(MerchantNotFoundException.class).when(merchantService).throwIfMerchantNotFoundByPublicId(anyString());

        assertThrows(MerchantNotFoundException.class, () ->
                productService.updateProductFullyWithMerchantId(merchantPublicId, productId, productDto));
    }

    @Test
    public void updateProductPatch_shouldUpdateProduct_whenProductFound(){
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);

        ProductDto actualUpdatedProductDto = productService.updateProductPatch(productId, productDto);

        assertThat(actualUpdatedProductDto).isEqualTo(productDto);
    }

    @Test
    public void updateProductPatch_shouldThrow_whenProductNotFound(){
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProductPatch(productId, productDto));
    }

    @Test
    public void updateProductPatchWithMerchantId_shouldThrow_whenMerchantNotFound(){
        doThrow(MerchantNotFoundException.class).when(merchantService).throwIfMerchantNotFoundByPublicId(anyString());

        assertThrows(MerchantNotFoundException.class, () ->
                productService.updateProductPatchWithMerchantId(merchantPublicId, productId, productDto));
    }

    @Test
    public void findById_shouldReturn_whenProductFound(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);

        ProductDto actualProduct = productService.findById(productId);

        assertThat(actualProduct).isEqualTo(productDto);
    }

    @Test
    public void findById_shouldThrow_thenProductNotFound(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.findById(productId));
    }

    @Test
    public void findByIdWithMerchantId_shouldThrow_whenMerchantNotFound(){
        doThrow(MerchantNotFoundException.class).when(merchantService).throwIfMerchantNotFoundByPublicId(anyString());

        assertThrows(MerchantNotFoundException.class, () ->
                productService.findByIdWithMerchantId(merchantPublicId, productId));
    }

    @Test
    public void findByIdWithMerchantId_shouldThrow_whenProductNotFoune(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.mapTo(any(Product.class))).thenReturn(productDto);

        ProductDto actualProduct = productService.findByIdWithMerchantId(merchantPublicId, productId);

        assertThat(actualProduct).isEqualTo(productDto);
    }

    @Test
    public void throwIfProductNotFound_shouldNotThrow_whenProductFound(){
        when(productRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> productService.throwIfProductNotFound(productId));
    }

    @Test
    public void throwIfProductNotFound_shouldThrow_whenProductNotFound(){
        assertThrows(ProductNotFoundException.class, () ->
                productService.throwIfProductNotFound(productId));
    }
}
