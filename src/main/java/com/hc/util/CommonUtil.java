package com.hc.util;

import javax.servlet.http.HttpServletRequest;

public final class CommonUtil {

	private static final String X_USER_ID = "x-user-id";
	private static final String X_LANG_ID = "x-lang-id";
	private static final String X_TOKEN = "x-token";
	
	private CommonUtil() {
		// prevent instantiation
	}
	
	public static long GetUserIDFromRequestHeader(HttpServletRequest request) {
		return Long.valueOf(request.getHeader(X_USER_ID));
	}
	
	public static long GetLanguageIDFromRequestHeader(HttpServletRequest request) {
		return Long.valueOf(request.getHeader(X_LANG_ID));
	}
	
	public static String GetTokenFromRequestHeader(HttpServletRequest request) {
		return request.getHeader(X_TOKEN);
	}
}
