package com.example.ecommercemarketplace.aspects;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @After("execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.handleSideExceptions(..))")
    public void handleSideExceptionsAdvice(JoinPoint joinPoint) {
        Exception exception = (Exception) joinPoint.getArgs()[0];
        Sentry.captureException(exception);
        log.error("Caught exception: {}", exception.toString());
    }

    @After("execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.*(..)) "  +
            "&& !execution(* com.example.ecommercemarketplace.exceptions.handlers.CustomGlobalExceptionHandler.handleSideExceptions(..))")
    public void handleOtherExceptionsAdvice(JoinPoint joinPoint) {
        Exception exception = (Exception) joinPoint.getArgs()[0];
        log.warn("Caught exception: {}", exception.toString());
    }
}
