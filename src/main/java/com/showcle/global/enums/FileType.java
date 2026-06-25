package com.showcle.global.enums;

public enum FileType {
    MEMBER_PROFILE("/member_profile");

    final String path;

    FileType(String path) {
        this.path = path;
    }
    public String getPath() { return path; }
}
