package com.showcle.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Member {

    private long idx;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String passwd;
    @NotEmpty
    private String name;
    @NotEmpty
    private String countryCode;
    @NotEmpty
    private String phone;
    private String device;
    private String pushToken;
    private MultipartFile profileImgFile;
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
}
