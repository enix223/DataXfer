package com.cel.dataxfer.kit;

public class DateFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7206200835454956227L;

	public DateFormatException(String message){
		super(message);
	} 
	
	public DateFormatException(Throwable cause){
		super(cause);
	}
	
	public DateFormatException(String message, Throwable cause){
		super(message, cause);
	}
}
