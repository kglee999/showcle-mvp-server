package com.showcle.global.util;

import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

public final class CommonUtil {

	// 이메일 인증번호 전송
	public static String generateAuthCode() {
		Random random = new SecureRandom();
		return String.format("%04d", random.nextInt(10000));
	}

	// 입력 항목 숫자 빼고 삭제
	public static String removeNonNumber(String input) {
		if(StringUtils.isEmpty(input)) return "";

		return input.replaceAll("[^0-9]", "");
	}

	public static String printException(Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append(e.toString()).append("\n");
		for (StackTraceElement traceElement : e.getStackTrace())
			builder.append(traceElement.toString()).append("\n");
		
		return builder.toString();
	}
}
