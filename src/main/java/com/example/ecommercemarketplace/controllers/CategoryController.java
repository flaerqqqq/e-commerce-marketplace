package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.mappers.impls.ProductMapper;
import com.example.ecommercemarketplace.services.CategoryService;
import com.example.ecommercemarketplace.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    @GetMapping("/{id}")
    public CategoryDto findCategoryById(@PathVariable("id") Long id){
        return categoryService.findById(id);
    }

    @GetMapping
    public Page<CategoryDto> findAllCategories(Pageable pageable){
        return categoryService.findAllCategories(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto){
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(categoryRequestDto, categoryDto);

        return categoryService.createCategory(categoryDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategoryFully(@RequestBody @Valid CategoryRequestDto categoryRequestDto,
                                           @PathVariable("id") Long id){
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(categoryRequestDto, categoryDto);

        return categoryService.updateCategoryFully(id, categoryDto);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategoryPatch(@RequestBody CategoryPatchUpdateRequestDto updateRequestDto,
                                           @PathVariable("id") Long id){
        CategoryDto categoryDto = new CategoryDto();
        BeanUtils.copyProperties(updateRequestDto, categoryDto);

        return categoryService.updateCategoryPatch(id ,categoryDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategoryById(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }

    @GetMapping("{id}/products")
    public Page<ProductResponseDto> findProductsByCategoryId(@PathVariable("id") Long id,
                                                             Pageable pageable){
        return productService.findPageOfProductByCategory(id, pageable)
                .map(productMapper::toResponseDto);
    }


}
