package com.sys.manage.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@Aspect
public class LoggerAspect {

    @Pointcut("execution( * com.sys.manage.controller.*.*(..))")
    public void myPointCut(){

    }

    @Around("myPointCut()")
    public Object myLogger(ProceedingJoinPoint pjp) throws Throwable {
        String className = Objects.requireNonNull(pjp.getTarget()).getClass().toString();
        String methodName = pjp.getSignature().getName();
        String name = pjp.getSignature().getName();
        if(!"uploadImage".equals(name)){
            Object args = pjp.getArgs();
            log.info("调用接口为"+className+":"+methodName+":"+new ObjectMapper().writeValueAsString(args));
        }
        Object obj = pjp.proceed();
        log.info("返回结果为"+":"+new ObjectMapper().writeValueAsString(obj));
        return obj;
    }
}
