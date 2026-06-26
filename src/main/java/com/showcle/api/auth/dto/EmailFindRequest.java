package com.showcle.api.auth.dto;

import com.showcle.global.util.CommonUtil;
import jakarta.validation.constraints.NotEmpty;

public record EmailFindRequest (
    @NotEmpty String name,
    @NotEmpty String countryCode,
    @NotEmpty String phone
) {
    public EmailFindRequest {
        // 전화번호 숫자 빼고 모두 제거
        phone = CommonUtil.removeNonNumber(phone);
    }
}
