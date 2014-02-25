package com.cel.dataxfer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cel.dataxfer.kit.DateKit;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;

public class LogController extends AppController {

	/**
	 * View the log 
	 */
	@ActionKey("/viewlog")
	public void viewlog(){
		String dt = getPara("dt", "");
		if(dt.equals("")){
			dt = DateKit.today("yyyyMMdd");
		}
		
		Matcher m = Pattern.compile("\\d{8}").matcher(dt);
		if(!m.find()){
			setAttr("msg", "Date format not correct");
			renderVelocity("log.html");
			return;
		}
		
		ArrayList<String> log = new ArrayList<String>();
		try {
			File f = new File(PathKit.getWebRootPath().replace("\\", "/") + "/log/history_" + dt + ".log");
			FileInputStream fs = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fs);
			BufferedReader bf = new BufferedReader(isr);
			String line = "";
			while((line = bf.readLine()) != null){
				log.add(line);
			}
			bf.close();
			isr.close();
			fs.close();			
		} catch (IOException e) {
			setAttr("msg", "Log file not found for specified date: " + dt);
			renderVelocity("log.html");
			return;
		}
		
		setAttr("dt", dt);
		setAttr("log", log);
		renderVelocity("log.html");
	}
}
