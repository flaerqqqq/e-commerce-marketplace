package com.example.ecommercemarketplace.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class UserLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.controllers.UserController.findByPublicId(..))" +
            "&& args(publicId)")
    public void afterFindByPublicId(String publicId) {
        log.info("User with publicId={} was retrieved.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.UserController.findAll(..))")
    public void afterFindAll() {
        log.info("All users were retrieved.");
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.UserController.updateUserFully(..))" +
            "&& args(publicId)")
    public void afterUpdateUserFully(String publicId) {
        log.info("User with publicId={} was fully updated.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.UserController.updateUserPatch(..))" +
            "&& args(publicId)")
    public void afterUpdateUserPatch(String publicId) {
        log.info("User with publicId={} was partially updated.", publicId);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.UserController.deleteUser(..))" +
            "&& args(publicId)")
    public void afterDeleteUser(String publicId) {
        log.info("User with publicId={} was deleted.", publicId);
    }
}
