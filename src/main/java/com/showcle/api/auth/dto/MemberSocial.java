package com.showcle.api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberSocial {
    private long idx;
    private long memberIdx;
    private String provider;
    private String providerUserId;
    private String email;
    private LocalDateTime createdAt;
}