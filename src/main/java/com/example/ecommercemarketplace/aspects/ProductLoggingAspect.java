package com.example.ecommercemarketplace.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ProductLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.controllers.ProductController.getAllProducts(..))")
    public void afterGetAllProducts() {
        log.info("Someone fetched all products with pagination.");
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.ProductController.searchProductsByName(..))" +
            "&& args(query)")
    public void afterSearchProductsByName(String query) {
        log.info("Someone searched products with query: '{}'", query);
    }
}
