package com.showcle.global.exception;

import com.showcle.global.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String text;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.name());
        this.text = errorCode.getText();
    }
}
