package com.hc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.hc.db.entity.Login;
/**
 * @author UBN
 * @since 15-05-2016
 */
public final class TokenUtil {

	private static final String MAGIC_KEY = "";
	
	private TokenUtil() {
		// prevent instantiation
	}

	public static String generateToken(Login user) {
	    /* expires in one hour */
		long expires = System.currentTimeMillis() + 1000 * 60 * 60;

		return user.getUser_name() + ":" + expires + ":" + computeSignature(user.getUser_name(), expires);
	}

	public static String parseUserIdentification(String token) {
		if (token == null) {
			return null;
		}

		String[] parts = token.split(":", 2);
		return parts.length > 0 ? parts[0] : null;
	}

	public static boolean isTokenValid(String token) {
		try {
			String[] parts = token.split(":");
			if (parts.length == 3) {
				String identification = parts[0];
				long expires = Long.parseLong(parts[1]);
				String signature = parts[2];

				//return expires >= System.currentTimeMillis() && signature.equals(computeSignature(identification, expires));
				
				return signature.equals(computeSignature(identification, expires));
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private static String computeSignature(String identification, long expires) {
		String signature = identification + ':' + expires + ':' + MAGIC_KEY;

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available");
		}

		return new String(Hex.encodeHex(digest.digest(signature.getBytes())));
	}
}
