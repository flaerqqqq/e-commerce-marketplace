package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategoryFully(Long id, CategoryDto categoryDto);

    CategoryDto updateCategoryPatch(Long id, CategoryDto categoryDto);

    Page<CategoryDto> findAllCategories(Pageable pageable);

    void deleteCategory(Long id);

    CategoryDto findById(Long id);
}
