package com.showcle.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeRequest {

    @NotEmpty
    private String email;
    @NotEmpty
    private String code;
}
