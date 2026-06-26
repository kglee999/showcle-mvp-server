package com.showcle.api.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberAuth {
    private long idx;
    private String email;
    private String code;
    private LocalDateTime sendDt;
    private LocalDateTime authDt;
    private LocalDateTime expiredDt;
}
