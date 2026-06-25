package com.showcle.global.enums;

import lombok.Getter;

@Getter
public enum FileType {
    MEMBER_PROFILE("/member_profile");

    private final String path;

    FileType(String path) {
        this.path = path;
    }
}
