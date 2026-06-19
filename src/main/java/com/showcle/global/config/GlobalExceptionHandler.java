package com.showcle.global.config;

import com.showcle.global.model.JsonResponse;
import com.showcle.global.enums.ServiceResult;
import com.showcle.global.exception.CustomValidationException;
import com.showcle.global.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // spring validation 오류시
    @ExceptionHandler(CustomValidationException.class)
    protected ResponseEntity<JsonResponse<Object>> handleValidationException(CustomValidationException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new JsonResponse<>(false, ServiceResult.INVALID_PARAM.name(), e.getErrors()), HttpStatus.BAD_REQUEST);
    }

    // 권한 없음
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<JsonResponse<Object>> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        log.error(CommonUtil.printException(e));
        return new ResponseEntity<>( new JsonResponse<>(false, ServiceResult.UNAUTHORIZED.name(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    // ConstraintValidator 오류시
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<JsonResponse<Object>> handleAccessDeniedException(HandlerMethodValidationException e) {
        log.error(CommonUtil.printException(e));
        return new ResponseEntity<>( new JsonResponse<>(false, ServiceResult.BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 전체 에러
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<JsonResponse<Object>> handleBusinessException(Exception e) {
        log.error(CommonUtil.printException(e));
        return new ResponseEntity<>( new JsonResponse<>(false, ServiceResult.FAIL.name(), e.getClass()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
