package com.cel.dataxfer.jython;

public class JythonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 997064403667428758L;

	public JythonException(){
		super();
	}
	
	public JythonException(Throwable e){
		super(e);
	}
	
	public JythonException(String msg){
		super(msg);
	}
	
	public JythonException(String msg, Throwable e){
		super(msg, e);
	}
}
