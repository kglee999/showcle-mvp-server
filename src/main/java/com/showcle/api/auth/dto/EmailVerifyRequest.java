package com.showcle.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;

// 이메일 코드 검증 DTO
public record EmailVerifyRequest (
    @NotEmpty String email,
    @NotEmpty String code
){}
