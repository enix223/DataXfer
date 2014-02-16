package com.cel.dataxfer.controller;

import java.io.File;
import java.util.ArrayList;

import com.cel.dataxfer.core.iSeries;
import com.cel.dataxfer.kit.Excel;
import com.cel.dataxfer.model.Config;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StringKit;


public class UserPackage extends AppController {
	
	@ActionKey("/ptos")
	public void ptos(){
		renderVelocity("ptos.html");
	}
	
	@ActionKey("/ptosReports")
	public void ptosReports(){
		String script = "LIBJ12COG/CUSALR";
		String message = iSeries.runScript(script);
		setAttr("message", message);
		renderVelocity("ptos.html");
	}
	
	/*
	 * 导入PTOS文件传输配置
	 */
	@ActionKey("/ptosConfig")
	public void ptosConfig(){
		//获取ptos_reports.xls
		String path = PathKit.getWebRootPath().replace("\\", "/") + "/config/ptos_reports.xls";
		File excelFile = new File(path);
		if(!excelFile.exists()){
			setAttr("message", "File not exist, " + path);
			renderVelocity("ptos.html");
			return;
		}
		
		Excel excel = new Excel(excelFile);
		int rows = excel.getMaxRows(0);
		
		//验证excel的模板格式是否符合要求
		ArrayList<String> header = new ArrayList<String>();
		header.add("source");
		header.add("sql");
		header.add("target");
		header.add("truncate");
		header.add("process");
		header.add("package");
		
		if(!excel.validateTemplate(0, header)){
			setAttr("message", "Excel template format not correct, please check. [" + path + "]");
			renderVelocity("ptos.html");
			return;
		}
		
		//插入数据传输条目
		for (int i = 1; i < rows; i++) {
			String source = excel.getValueToStr(0, i, 0);
			String sql = excel.getValueToStr(0, i, 1);
			String target = excel.getValueToStr(0, i, 2);
			String truncate = excel.getValueToStr(0, i, 3);
			String process = excel.getValueToStr(0, i, 4);
			String pkg = excel.getValueToStr(0, i, 5);
			
			if(StringKit.isBlank(source)){
				break; //break if reach the last rows.
			}		
			
			Config entryConfig = new Config();
			entryConfig.set("source", source);
			entryConfig.set("sql", sql);
			entryConfig.set("target", target);
			entryConfig.set("truncate", truncate);
			entryConfig.set("process", process);
			entryConfig.set("year", "*");
			entryConfig.set("month", "*");
			entryConfig.set("day", "*");
			entryConfig.set("weekday", "*");
			entryConfig.set("specify", "");
			entryConfig.set("pkg", pkg);
			
			entryConfig.save();
		}
		
		setAttr("message", "PTOS config entries import success. [" + path + "]");
		renderVelocity("ptos.html");
	}
}














