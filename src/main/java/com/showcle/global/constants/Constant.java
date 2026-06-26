package com.showcle.global.constants;

import java.time.format.DateTimeFormatter;

public class Constant {

    // 디렉토리 생성 날짜 형식
    public static final DateTimeFormatter DATE_FORMATTER1 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 실행 파일 및 특정 확장자는 업로드되지 않도록 처리
    public static final String[] BAD_EXTENSION = { "jsp", "php", "asp", "html", "perl", "exe", "class", "js", "lnk", "pif", "msi", "vbs", "inf", "reg"};

}
