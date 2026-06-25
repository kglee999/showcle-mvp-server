package com.showcle.global.enums;

public class Auth {

    public enum MailType {
        EMAIL_VERIFY("인증 메일 발송", "mail/mail-verify.html");

        final String subject;
        final String htmlPath;

        MailType(String subject, String htmlPath) {
            this.subject = subject;
            this.htmlPath = htmlPath;
        }
        public String getSubject() { return subject; }
        public String getHtmlPath() { return htmlPath; }
    }
}
