package com.demo.users.api.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class Validator {

    @Around("@annotation(com.demo.users.api.validation.RequestParamValidator))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        RequestParamValidator audit = getMethodAnnotation(joinPoint, RequestParamValidator.class);
        audit.value().getDeclaredConstructor().newInstance().validate(args);
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }
}
