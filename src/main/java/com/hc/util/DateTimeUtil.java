package com.hc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author fivedev
 * @since 12-05-2016
 */
public final class DateTimeUtil {

	private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

	private DateTimeUtil() {
		// prevent instantiation
	}
	
	/**
	 * @param string_date
	 * @return
	 */
	public static Calendar getDateFromString(String string_date){
		return getDateFromString(string_date, null);
	}
	
	/**
	 * @param string_date
	 * @param date_format
	 * @return
	 */
	private static Calendar getDateFromString(String string_date, String date_format) {

		try {
			Calendar cal = Calendar.getInstance();
			if (date_format != null) {
				SimpleDateFormat sdf2 = new SimpleDateFormat(date_format);
				cal.setTime(sdf2.parse(string_date));
			}else{
				cal.setTime(sdf1.parse(string_date));
			} 
			return cal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static String getFormattedDate(Calendar date){
		return getFormattedDate(date, null);
	}
	
	/**
	 * @param date
	 * @param format
	 * @return
	 */
	private static String getFormattedDate(Calendar date, String format) {
		
		try {
			if(format != null || format.trim().length()==0){
				return sdf1.format(date.getTime());
			}else{
				SimpleDateFormat sdf2 = new SimpleDateFormat(format);
				return sdf2.format(date.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
