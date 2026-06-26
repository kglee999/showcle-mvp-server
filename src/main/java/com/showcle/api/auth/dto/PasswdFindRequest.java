package com.showcle.api.auth.dto;

import jakarta.validation.constraints.NotEmpty;

public record PasswdFindRequest (
    @NotEmpty String email,
    @NotEmpty String name
) {}
