package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategoryFully(Long id, CategoryDto categoryDto);

    CategoryDto updateCategoryPatch(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto findById(Long id);
}
