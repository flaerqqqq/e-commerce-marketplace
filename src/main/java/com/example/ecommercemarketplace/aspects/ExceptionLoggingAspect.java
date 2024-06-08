package com.example.ecommercemarketplace.aspects;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.handleSideExceptions(..))" +
            "&& args(ex)")
    public void handleSideExceptionsAdvice(Exception ex) {
        Sentry.captureException(ex);
        log.error("Caught exception: {}", ex.toString());
    }

    @After("execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.*(..)) " +
            "&& !execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.handleSideExceptions(..))" +
            "&& args(ex)")
    public void handleOtherExceptionsAdvice(Exception ex) {
        log.warn("Caught exception: {}", ex.toString());
    }
}
