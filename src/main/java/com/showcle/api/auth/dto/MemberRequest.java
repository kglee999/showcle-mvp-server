package com.showcle.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class MemberRequest {

    private long idx;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String passwd;
    @NotEmpty
    private String name;
    private int grade;
    @NotEmpty
    private String countryCode;
    @NotEmpty
    private String phone;
    private MultipartFile profileImgFile;
    private long profileImg;
    private int pushYn;
    private String createdBy;
    private String updatedBy;
}
