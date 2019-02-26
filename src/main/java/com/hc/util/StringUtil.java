package com.hc.util;

import org.apache.commons.codec.binary.Base64;

/**
 * @author fivedev
 * @since 12-05-2016
 */
public final class StringUtil {

	private StringUtil() {
		// prevent instantiation
	}

	/**
	 * @param str
	 * @return true if str is Double
	 */
	public static Double toDoubleVal(String str) {
		return Double.parseDouble(str);
	}

	/**
	 * @param str
	 * @return true if str is Integer
	 */
	public static int toIntegerValue(String str) {
			return Integer.parseInt(str);
	}

	/**
	 * @param str
	 * @return true if str in not null
	 */
	public static boolean isStringEmpty(String str){
		if(str == null || str.length() == 0){
			return true;
		}
		return false;
	}

	/**
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encryptPassword(String password) throws Exception {
		byte[] encodedBytes = Base64.encodeBase64(password.getBytes());
		return new String(encodedBytes);
	}

	/**
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String decryptPassword(String password) throws Exception {
		byte[] decodedBytes = Base64.decodeBase64(password.getBytes());
		return new String(decodedBytes);
	}
}
