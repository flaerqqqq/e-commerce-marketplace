package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.dto.CategoryPatchUpdateRequestDto;
import com.example.ecommercemarketplace.dto.CategoryRequestDto;
import com.example.ecommercemarketplace.dto.ProductResponseDto;
import com.example.ecommercemarketplace.mappers.impls.ProductMapper;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public CategoryDto findCategoryById(@PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @GetMapping
    public Page<CategoryDto> findAllCategories(Pageable pageable) {
        return categoryService.findAllCategories(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto categoryRequest) {
        CategoryDto categoryDto = modelMapper.map(categoryRequest, CategoryDto.class);
        return categoryService.createCategory(categoryDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategoryFully(@RequestBody @Valid CategoryRequestDto updateRequest,
                                           @PathVariable("id") Long id) {
        CategoryDto categoryDto = modelMapper.map(updateRequest, CategoryDto.class);
        return categoryService.updateCategoryFully(id, categoryDto);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategoryPatch(@RequestBody CategoryPatchUpdateRequestDto updateRequest,
                                           @PathVariable("id") Long id) {
        CategoryDto categoryDto = modelMapper.map(updateRequest, CategoryDto.class);
        return categoryService.updateCategoryPatch(id, categoryDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("{id}/products")
    public Page<ProductResponseDto> findAllProductsByCategoryId(@PathVariable("id") Long id,
                                                             Pageable pageable) {
        return productService.findPageOfProductByCategory(id, pageable)
                .map(productMapper::toResponseDto);
    }
}
