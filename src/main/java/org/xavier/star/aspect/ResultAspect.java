package org.xavier.star.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.xavier.star.common.Result;

import java.util.Objects;

@Aspect
@Component
@Slf4j
public class ResultAspect {
    @Around("execution(public * org.xavier.star.controller.*.*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
            Object object = proceedingJoinPoint.proceed();
            if(Objects.isNull(object) || !object.getClass().equals(Result.class)){
                object = Result.success(object);
            }
            return object;
    }
}

