package com.showcle.global.controller;

import com.showcle.global.exception.CustomValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CommonController {

    // Validation 에러 체크
    protected void checkValidation(Errors errors) {
        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }
    }

    // Validation 에러 TEXT 로 추출
    protected Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = error.getField();
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
}
