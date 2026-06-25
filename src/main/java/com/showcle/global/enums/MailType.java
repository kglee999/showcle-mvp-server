package com.showcle.global.enums;

import lombok.Getter;

@Getter
public enum MailType {
    EMAIL_VERIFY("인증 메일 발송", "mail/mail-verify.html");

    private final String subject;
    private final String htmlPath;

    MailType(String subject, String htmlPath) {
        this.subject = subject;
        this.htmlPath = htmlPath;
    }
}
