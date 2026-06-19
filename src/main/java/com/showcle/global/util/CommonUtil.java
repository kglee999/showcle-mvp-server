package com.showcle.global.util;

import java.security.SecureRandom;

public final class CommonUtil {

	// 대문자 A-Z + 숫자 0-9 문자셋
	private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 8;
	private static final SecureRandom random = new SecureRandom();

	public static String printException(Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append(e.toString()).append("\n");
		for (StackTraceElement traceElement : e.getStackTrace())
			builder.append(traceElement.toString()).append("\n");
		
		return builder.toString();
	}

	// 인플루언서 관리 - code 생성(E3VBHAJ2  패턴)
	public static String generateCode() {
		StringBuilder sb = new StringBuilder(CODE_LENGTH);
		for (int i = 0; i < CODE_LENGTH; i++) {
			int index = random.nextInt(CHAR_POOL.length());
			sb.append(CHAR_POOL.charAt(index));
		}
		return sb.toString();
	}
}
