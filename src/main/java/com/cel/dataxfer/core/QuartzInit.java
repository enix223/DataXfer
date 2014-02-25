package com.cel.dataxfer.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzInit extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6532041841254443503L;

	public void init(ServletConfig config) throws ServletException{		
		String prefix = config.getServletContext().getRealPath("/").replace("\\", "/");
        String file = config.getInitParameter("quartz:config-file"); 
        String config_path = prefix + file; 
        
		Properties properties = new Properties();
		try {
			System.out.println("quartz properties file loaded: " + config_path);
			FileInputStream is = new FileInputStream(config_path);
			properties.load(is);
			String jobFileName = properties.getProperty("org.quartz.plugin.jobInitializer.fileNames");
			properties.setProperty("org.quartz.plugin.jobInitializer.fileNames", prefix + jobFileName);
			is.close();
			
			//init the quartz with quartz.properties file 
			StdSchedulerFactory factory = new StdSchedulerFactory();
			factory.initialize(properties);
			Scheduler scheduler = factory.getScheduler();
			scheduler.start();
			
		} catch (IOException e) {
			System.out.println("quartz properties file not found. [" + config_path + "]");
		} catch (SchedulerException e) {
			System.out.println("quartz config failed. [" + config_path + "]");
			e.printStackTrace();
		}
	}
	
}
