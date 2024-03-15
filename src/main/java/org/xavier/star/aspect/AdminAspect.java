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
public class AdminAspect {

    @Autowired
    SessionUtils sessionUtil;

    @Around("@annotation(org.xavier.star.annotation.Admin)")
    public Object doAroundAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

        SessionData sessionData = sessionUtil.getSessionData();

        AssertUtil.isNotNull(sessionData, CommonErrorCode.INVALID_SESSION);

        AssertUtil.isTrue(sessionData.getType() == 1,CommonErrorCode.USER_NOT_ADMIN);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Auth annotation = method.getAnnotation(Auth.class);

        //log
        log.error("------------");
        log.error("operator: " + sessionData.getId());
        log.error("operation: " + method.getName());

        return joinPoint.proceed();
    }
}
