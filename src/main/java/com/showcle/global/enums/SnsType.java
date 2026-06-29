package com.showcle.global.enums;

import lombok.Getter;

@Getter
public enum SnsType {
    KAKAO("kakao"),
    GOOGLE("google"),
    APPLE("apple");

    private String name;

    SnsType(String name) {
        this.name = name;
    }

    public static SnsType getByName(String name) {
        for (SnsType snsType : SnsType.values()) {
            if (snsType.name.equals(name)) {
                return snsType;
            }
        }
        return null;
    }
}