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

	// 이메일 찾기 결과 이메일 마스킹
	// 이메일 아이디는 세번째 자리부터, 도메인 두번째 자리부터 마스킹
	public static String emailMasking(String email) {
		if(StringUtils.isEmpty(email)) return email;

		String[] emailArr = email.split("@");

		if(emailArr.length != 2) {
			return masking(email, 2, email.length());
		}

		// 아이디 처리
		String id = masking(emailArr[0], 2, 0);
		String domain = masking(emailArr[1], 1, emailArr[1].indexOf('.'));
		return id + "@" + domain;
	}

	// 마스킹 처리 함수
	public static String masking(String input, int start, int end) {
		if(StringUtils.isEmpty(input)) return input;

		if(end == 0 || end > input.length()) end = input.length();
		if(start < 0) start = 0;

		char[] ch = input.toCharArray();

		for(int i = start; i < end; i++) {
			ch[i] = '*';
		}
		return new String(ch);
	}

	public static String printException(Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append(e.toString()).append("\n");
		for (StackTraceElement traceElement : e.getStackTrace())
			builder.append(traceElement.toString()).append("\n");
		
		return builder.toString();
	}
}
