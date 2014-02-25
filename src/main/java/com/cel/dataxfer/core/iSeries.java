package com.cel.dataxfer.core;

import java.beans.PropertyVetoException;
import java.util.Map;
import java.util.Map.Entry;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;


/**
 * iSeries helper to access AS400 resources
 * @author Enix
 *
 */
public class iSeries {
	
	public static final String HOST =  "192.168.231.144";
	public static final String USER = "NOELT";
	public static final String PASSWORD = "NOEL123";
	public static final String DRIVER = "com.ibm.as400.access.AS400JDBCDriver";
	
	private static AS400 connection;
	private static AS400 static_conn;
	
	/**
	 * Get the AS400 connection
	 * @return
	 */
	public static AS400 getConnection(){
		connection = new AS400(HOST);
		return connection;
	}
	
	/** 
	 * Get the static connection for internal use.
	 * @return
	 */
	private static AS400 getStaticConnection(){
		if(static_conn == null){
			static_conn = new AS400(HOST, USER, PASSWORD);
			try {
				static_conn.setCcsid(1388);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}			
		return static_conn;
	}
	
	/**
	 * Validate the username and password
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean validate(String username, String password){
		if(connection == null){
			connection = getConnection();			
		}
		
		try {
			connection.setUserId(username);
			connection.setPassword(password);
			if(connection.validateSignon()){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {				
		}		
		return false;
	}
	
	/**
	 * Run the sequel script in AS400
	 * @param script
	 * @return
	 */
	public static String runScript(String script){
		String cmdText = "SEQUEL/RUNSCRIPT SCRIPT("+ script + ")";
		return runCommand(cmdText);
	}
	
	/**
	 * Run the sequel script with variables.
	 * @param script
	 * @param params
	 * @return
	 */
	public static String runScript(String script, Map<String, Object> params){
		if(params.size() == 0){
			return runScript(script);
		}
		
		String paramStr = buildVariables(params);
				
		String cmdText = "SEQUEL/RUNSCRIPT SCRIPT("+ script + ")";
		if(paramStr.length() > 0){
			cmdText += " SETVAR(" + paramStr + ")";
		}
		return runCommand(cmdText);
	}
	
	/**
	 * Run Sequel update script.
	 * @param fromSQL
	 * @param params
	 * @return
	 */
	public static String sequelUpdate(String fromSQL, Map<String, Object> params){
		if(params.size() == 0){
			return "Update failed. No columns specified.";
		}
		
		StringBuilder paramStr = new StringBuilder();
		for(Entry<String, Object> p : params.entrySet()){
			if(p.getValue() instanceof String){
				paramStr.append("(" + p.getKey() + " \'\"" + String.valueOf(p.getValue()) + "\"\')");
			} else {
				paramStr.append("(" + p.getKey() + " " + String.valueOf(p.getValue()) + ")");
			}
		}
		
		String cmdText = "SEQUEL/UPDATE SET(" + paramStr.toString() + ") SQL('" + fromSQL + "')";
		return runCommand(cmdText);
	}
	
	/**
	 * Run the remote command in AS400
	 * @param cmdText
	 * @return messages from AS400
	 */
	public static String runCommand(String cmdText){
		connection = getStaticConnection();
		
		StringBuilder returnMsg = new StringBuilder();
		CommandCall cmd = new CommandCall(connection);
		try {
			cmd.run(cmdText);
			AS400Message[] messages = cmd.getMessageList();
			for(AS400Message msg : messages){
				returnMsg.append(msg.getText());
				returnMsg.append("\n");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}	
				
		return returnMsg.toString();
	}
	
	/*
	 * Build variable for RUNSCRIPT command 
	 */
	private static final String buildVariables(Map<String, Object> params){
		StringBuilder paramStr = new StringBuilder();
		for(Entry<String, Object> p : params.entrySet()){
			if(p.getValue() instanceof String){
				paramStr.append("(&" + p.getKey() + " \'\"" + String.valueOf(p.getValue()) + "\"\')");
			} else {
				paramStr.append("(&" + p.getKey() + " " + String.valueOf(p.getValue()) + ")");
			}
		}
		return paramStr.toString();
	}
	
}














