package com.effectivemobile.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class AppAspect {


    @Around("within(com.effectivemobile.controller..*)")
    public Object aroundCallControllersMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        var requestId = UUID.randomUUID().toString();
        try (MDC.MDCCloseable ignored1 = MDC.putCloseable("RequestId", requestId)) {
            result = joinPoint.proceed();
            log.info("Called method " + joinPoint.getSignature().getName());
        }
        return result;
    }
}