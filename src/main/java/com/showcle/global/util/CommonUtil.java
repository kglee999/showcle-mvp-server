package com.showcle.global.util;

import java.security.SecureRandom;
import java.util.Random;

public final class CommonUtil {

	public static String generateAuthCode() {
		Random random = new SecureRandom();
		return String.format("%04d", random.nextInt(10000));
	}

	public static String printException(Exception e) {
		StringBuilder builder = new StringBuilder();
		builder.append(e.toString()).append("\n");
		for (StackTraceElement traceElement : e.getStackTrace())
			builder.append(traceElement.toString()).append("\n");
		
		return builder.toString();
	}
}
