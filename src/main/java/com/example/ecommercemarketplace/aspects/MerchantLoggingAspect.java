package com.example.ecommercemarketplace.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MerchantLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.findByPublicId(..))" +
            " && args(publicId)")
    public void afterFindByPublicId(String publicId) {
        log.info("Merchant with publicId={} was retrieved.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.findAllMerchants(..))")
    public void afterFindAllMerchants() {
        log.info("All merchants were retrieved.");
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.updateMerchantFully(..))" +
            " && args(publicId)")
    public void afterUpdateMerchantFully(String publicId) {
        log.info("Merchant with publicId={} was fully updated.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.updateMerchantPatch(..))" +
            " && args(publicId)")
    public void afterUpdateMerchantPatch(String publicId) {
        log.info("Merchant with publicId={} was partially updated.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.deleteMerchant(..))" +
            " && args(publicId)")
    public void afterDeleteMerchant(String publicId) {
        log.info("Merchant with publicId={} was deleted.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.getProductsByMerchant(..))" +
            " && args(publicId)")
    public void afterGetProductsByMerchant(String publicId) {
        log.info("Products for merchant with publicId={} were retrieved.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.getProductById(..))" +
            " && args(publicId, productId)")
    public void afterGetProductById(String publicId, Long productId) {
        log.info("Product with id={} for merchant with publicId={} was retrieved.", productId, publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.createProduct(..))" +
            " && args(publicId)")
    public void afterCreateProduct(String publicId) {
        log.info("Product was created for merchant with publicId={}.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.updateProductFully(..))" +
            " && args(publicId, productId)")
    public void afterUpdateProductFully(String publicId, Long productId) {
        log.info("Product with id={} for merchant with publicId={} was fully updated.", productId, publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.updateProductPatch(..))" +
            " && args(publicId, productId)")
    public void afterUpdateProductPatch(String publicId, Long productId) {
        log.info("Product with id={} for merchant with publicId={} was partially updated.", productId, publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantController.deleteProduct(..))" +
            " && args(publicId, productId)")
    public void afterDeleteProduct(String publicId, Long productId) {
        log.info("Product with id={} for merchant with publicId={} was deleted.", productId, publicId);
    }
}
