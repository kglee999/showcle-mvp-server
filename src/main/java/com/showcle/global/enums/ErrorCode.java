package com.showcle.global.enums;

public enum ErrorCode {
    EMAIL_CODE_EXPRIED( "이메일 인증 코드 만료"),
    EMAIL_CODE_NOT_MATCH( "이메일 인증코드 불일치"),
    EMAIL_NOT_VERIFIED( "이메일 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_EXISTS("이메일이 이미 존재합니다."),
    FILE_UPLOAD_ERROR("파일 업로드 중 오류가 발생했습니다."),
    FILE_UPLOAD_BAD_EXTENSION("해당 업로드 파일은 업로드할 수 없는 파일입니다.");


    final String text;

    ErrorCode(String text) {
        this.text = text;
    }
    public String getText() { return text; }
}
