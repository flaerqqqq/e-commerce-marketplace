package com.example.ecommercemarketplace.aspects;


import com.example.ecommercemarketplace.dto.CategoryRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CategoryLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.findCategoryById(..))" +
            " && args(id)")
    public void afterFindCategoryById(Long id) {
        log.info("Category with id={} was retrieved.", id);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.findAllCategories(..))")
    public void afterFindAllCategories() {
        log.info("All categories were retrieved.");
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.createCategory(..))" +
            " && args(categoryRequest)")
    public void afterCreateCategory(CategoryRequestDto categoryRequest) {
        log.info("A new category with name={} was created.", categoryRequest.getName());
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.updateCategoryFully(..))" +
            " && args(id)")
    public void afterUpdateCategoryFully(Long id) {
        log.info("Category with id={} was fully updated.", id);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.updateCategoryPatch(..))" +
            " && args(id)")
    public void afterUpdateCategoryPatch(Long id) {
        log.info("Category with id={} was partially updated.", id);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.deleteCategoryById(..))" +
            " && args(id)")
    public void afterDeleteCategoryById(Long id) {
        log.info("Category with id={} was deleted.", id);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.CategoryController.findAllProductsByCategoryId(..))" +
            " && args(id)")
    public void afterFindAllProductsByCategoryId(Long id) {
        log.info("Products for category with id={} were retrieved.", id);
    }
}
