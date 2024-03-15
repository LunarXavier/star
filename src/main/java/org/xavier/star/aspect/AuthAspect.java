package org.xavier.star.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xavier.star.annotation.Auth;
import org.xavier.star.common.CommonErrorCode;
import org.xavier.star.dto.SessionData;
import org.xavier.star.util.AssertUtil;
import org.xavier.star.util.SessionUtils;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AuthAspect {

    @Autowired
    SessionUtils sessionUtil;

    @Around("@annotation(org.xavier.star.annotation.Auth)")
    public Object doAroundAuth(ProceedingJoinPoint joinPoint) throws Throwable {

        SessionData sessionData = sessionUtil.getSessionData();

        AssertUtil.isNotNull(sessionData, CommonErrorCode.INVALID_SESSION);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Auth annotation = method.getAnnotation(Auth.class);

        //log
        log.error("------------");
        log.error("operation: " + method.getName());

        return joinPoint.proceed();
    }


}
