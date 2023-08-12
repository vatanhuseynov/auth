package com.example.auth.aspect;

import com.example.auth.AppUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class RestControllerAspect {

    private static final String[] DEFAULT_PATH = {""};

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getPointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postPointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putPointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deletePointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(RequestMapping.class);
        var parameters = getParameters(joinPoint);
        if (classMapping != null && methodMapping != null) {
            var method = Arrays.stream(methodMapping.method()).map(RequestMethod::name).collect(Collectors.joining(", "));
            logBefore(concatPaths(classMapping.value(), methodMapping.value()), method, parameters);
        }
    }

    @Before("getPointcut()")
    public void beforeGet(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(GetMapping.class);
        var parameters = getParameters(joinPoint);
        if (classMapping != null && methodMapping != null)
            logBefore(concatPaths(classMapping.value(), methodMapping.value()), "GET", parameters);
    }

    @Before("postPointcut()")
    public void beforePost(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(PostMapping.class);
        var parameters = getParameters(joinPoint);
        if (classMapping != null && methodMapping != null)
            logBefore(concatPaths(classMapping.value(), methodMapping.value()), "POST", parameters);
    }

    @Before("putPointcut()")
    public void beforePut(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(PutMapping.class);
        var parameters = getParameters(joinPoint);
        if (classMapping != null && methodMapping != null)
            logBefore(concatPaths(classMapping.value(), methodMapping.value()), "PUT", parameters);
    }

    @Before("deletePointcut()")
    public void beforeDelete(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(DeleteMapping.class);
        var parameters = getParameters(joinPoint);
        if (classMapping != null && methodMapping != null)
            logBefore(concatPaths(classMapping.value(), methodMapping.value()), "DELETE", parameters);
    }

    public void logBefore(String[] path, String method, Map<String, Object> parameters) {
        try {
            log.info("==> path(s): {}, method(s): {}, user: {}, arguments: {} ",
                    path, method, getUser(), AppUtil.get().objectMapper().writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            log.error("logBefore error");
        }
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void after(JoinPoint joinPoint, Object entity) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(RequestMapping.class);
        if (classMapping != null && methodMapping != null) {
            var method = Arrays.stream(methodMapping.method()).map(RequestMethod::name).collect(Collectors.joining(", "));
            logAfter(concatPaths(classMapping.value(), methodMapping.value()), method, entity);
        }
    }

    @AfterReturning(pointcut = "getPointcut()", returning = "entity")
    public void afterGet(JoinPoint joinPoint, Object entity) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(GetMapping.class);
        if (classMapping != null && methodMapping != null)
            logAfter(concatPaths(classMapping.value(), methodMapping.value()), "GET", entity);
    }

    @AfterReturning(pointcut = "postPointcut()", returning = "entity")
    public void afterPost(JoinPoint joinPoint, Object entity) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(PostMapping.class);
        if (classMapping != null && methodMapping != null)
            logAfter(concatPaths(classMapping.value(), methodMapping.value()), "POST", entity);
    }

    @AfterReturning(pointcut = "putPointcut()", returning = "entity")
    public void afterPut(JoinPoint joinPoint, Object entity) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(PutMapping.class);
        if (classMapping != null && methodMapping != null)
            logAfter(concatPaths(classMapping.value(), methodMapping.value()), "PUT", entity);
    }

    @AfterReturning(pointcut = "deletePointcut()", returning = "entity")
    public void afterDelete(JoinPoint joinPoint, Object entity) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        var methodMapping = signature.getMethod().getAnnotation(DeleteMapping.class);
        if (classMapping != null && methodMapping != null)
            logAfter(concatPaths(classMapping.value(), methodMapping.value()), "DELETE", entity);
    }

    public void logAfter(String[] path, String method, Object entity) {
        try {
            log.info("<== path(s): {}, method(s): {}, user: {}, returning: {}",
                    path, method, getUser(), AppUtil.get().objectMapper().writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            log.error("logAfter error");
        }
    }

    private String[] concatPaths(String[] classPaths, String[] methodPaths) {
        if (classPaths.length == 0) {
            classPaths = DEFAULT_PATH;
        }
        if (methodPaths.length == 0) {
            methodPaths = DEFAULT_PATH;
        }
        var concatenatedPaths = new String[classPaths.length * methodPaths.length];
        var i = 0;
        for (var classPath : classPaths) {
            for (var methodPath : methodPaths) {
                concatenatedPaths[i] = classPath + methodPath;
                i++;
            }
        }
        return concatenatedPaths;
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        var signature = (CodeSignature) joinPoint.getSignature();
        Map<String, Object> map = new HashMap<>();
        var parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }
        return map;
    }

    private Object getUser() {

        return null;
    }
}

