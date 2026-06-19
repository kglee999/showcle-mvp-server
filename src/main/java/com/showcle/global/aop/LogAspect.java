package com.showcle.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {
    // .. 하위 모든 패키지(. 점 한개는 직계 하위 클래스) * 모든 클래스 . * 모든 메소드, (..) 모든 파라미터
    @Around("within(com.nubebe.controller..*)")
    public Object logAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();
        if(requestAttribute == null) return joinPoint.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) requestAttribute).getRequest();

        // 1. Request 로깅
        logRequest(request, joinPoint);
        // 2. 메소드 실행 및 시간 측정
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();
        stopWatch.stop();

        // 3. Response 로깅
        logResponse(result, stopWatch);
        return result;
    }

    private void logRequest(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        // HTTP 메소드, URI
        log.info("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());

        // 헤더 정보
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append(headerName).append(": ").append(request.getHeader(headerName));
            if(headerNames.hasMoreElements()) sb.append(", ");
        }
        log.debug("[HEADER] {}", sb);
        
        // 파라미터 정보
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null
                && ((arg instanceof Collection)
                    || (arg instanceof Map)
                    || (arg.getClass().getPackage().getName().startsWith("com.nubebe.model"))
            )) {
                log.info("[PARAMETER] {}: {}", arg.getClass().getSimpleName(), arg);
            }
        }
    }

    private void logResponse(Object result, StopWatch stopWatch) {
        if (result instanceof ResponseEntity<?> response) {
            log.info("[RESPONSE] {}", response.getStatusCode());

            // 응답 바디
            if (response.getBody() != null) {
                log.debug("[RESPONSE] body: {}", response.getBody());
            }
        }
        // 실행 시간
        log.info("[EXECUTION TIME] {} ms", stopWatch.getTotalTimeMillis());
    }
}
