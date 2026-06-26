package com.showcle.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.showcle.global.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Member {

    private long idx;
    private String email;
    private String passwd;
    private String name;
    private String countryCode;
    private String phone;
    private String device;
    private String pushToken;
    private long profileImg;

    private int pushYn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDt;
    private int discarded;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private String updatedBy;

    // 이메일 찾기 결과
    public record EmailFindResponse(String email, String createdDate) {
        public EmailFindResponse {
            // email - 마스킹 처리
            email = CommonUtil.emailMasking(email);
        }
    }
}
