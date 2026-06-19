package com.showcle.global.model;

import com.showcle.global.enums.ServiceResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonResponse<T> {
    private boolean result;
    private String message;
    private T data;

    public JsonResponse(T data) {
        this.result = true;
        this.message = ServiceResult.SUCCESS.name();
    }
}
