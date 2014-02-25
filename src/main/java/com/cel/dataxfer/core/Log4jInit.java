package com.cel.dataxfer.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * Log4jInit is to setup the log4j parameters at startup 
 * @author Enix
 *
 */
public class Log4jInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8694519675718407423L;

	
	public Log4jInit(){		
	}
	
	/**
	 * Setup the log file path
	 */
	public void init(ServletConfig config) throws ServletException{
		String prefix = config.getServletContext().getRealPath("/").replace("\\", "/");
        String file = config.getInitParameter("log4j"); 
        String config_path = prefix + file; 
        
		Properties properties = new Properties();
		try {
			System.out.println("log4j properties file loaded: " + config_path);
			FileInputStream is = new FileInputStream(config_path);
			properties.load(is);
			is.close();
			//Set the log file path
			String logFile = prefix + properties.getProperty("log4j.appender.FILE.File");			
			properties.setProperty("log4j.appender.FILE.File", logFile);
			PropertyConfigurator.configure(properties);
			System.out.println("log4j log files name: " + logFile);
		} catch (IOException e) {
			System.out.println("log4j properties file not found. [" + config_path + "]");
		}
	}
}
