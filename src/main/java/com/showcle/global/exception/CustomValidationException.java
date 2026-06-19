package com.showcle.global.exception;

import com.showcle.global.enums.ServiceResult;
import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super(ServiceResult.INVALID_PARAM.name());
        this.errors = errors;
    }
}
