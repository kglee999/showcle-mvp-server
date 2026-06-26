package com.showcle.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;

// 이메일 사용 가능 여부 체크 DTO
public record EmailRequest (
    @NotEmpty String email
){}
