package com.cel.dataxfer.controller;

import com.cel.dataxfer.jython.Transfer;
import com.jfinal.core.ActionKey;

public class Trigger extends AppController{

	@ActionKey("/manual")
	public void manual(){
		renderVelocity("manual.html");
	}
	
	@ActionKey("/trigger")
	public void trigger(){
		String pkg = getPara("pkg");
		if(isParaBlank("pkg")){
			setAttr("msg", "请输入Package名称");
			renderVelocity("manual.html");
			return;
		}
		
		int flag = Transfer.transfer(pkg);
		
		setAttr("rc", flag);
		setAttr("msgs", Transfer.getMsg());
		renderVelocity("manual.html");
	}
}
