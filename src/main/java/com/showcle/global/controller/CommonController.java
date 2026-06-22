package com.showcle.global.controller;

import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import com.showcle.global.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class CommonController {

    protected JsonResponse<Object> getResultJson(ServiceResult result, Object data) {
        return new JsonResponse<>(result == ServiceResult.SUCCESS, result.name(), data);
    }

    protected void logError(Exception e) {
        log.error(CommonUtil.printException(e));
    }

    protected Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }
}
