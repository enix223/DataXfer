package com.cel.dataxfer.kit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilities for Date object
 * @author Enix
 *
 */
public class DateKit {

	/**
	 * Get the date of today with specified format
	 * @param format
	 * @return
	 */
	public static final String today(String format){		
		Calendar calendar = Calendar.getInstance();
		Date todayDate = calendar.getTime();	    
	    try {
	    	DateFormat df = new SimpleDateFormat(format);
	    	return df.format(todayDate);
		} catch (Exception e) {
			throw new DateFormatException("Date format error.", e);
		}	    
	}
	
	/**
	 * Get today date with format yyyy-MM-dd
	 * @return String
	 */
	public static final String today(){
		return today("yyyy-MM-dd");
	}
	
	/**
	 * Get date before [before] days and return with format: [format] 
	 * @param before
	 * @param format
	 * @return
	 */
	public static final String getDateBefore(int before, String format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1 * before);
		Date beforeDate = calendar.getTime();
		try {
	    	DateFormat df = new SimpleDateFormat(format);
	    	return df.format(beforeDate);
		} catch (Exception e) {
			throw new DateFormatException("Date format error.", e);
		}	
	}
	
	/**
	 * Get date before and return yyyy-MM-dd format
	 * @param before
	 * @return
	 */
	public static final String getDateBefore(int before){
		return getDateBefore(before, "yyyy-MM-dd");
	}
	
	/**
	 * Get date after n days.
	 * @param after
	 * @return
	 */
	public static final String getDateAfter(int after){
		return getDateBefore(-1 * after);
	}
	
	/**
	 * Get date after n days and return date with format yyyy-MM-dd
	 * @param after
	 * @param format
	 * @return
	 */
	public static final String getDateAfter(int after, String format){
		return getDateBefore(-1 * after, format);
	}
}
