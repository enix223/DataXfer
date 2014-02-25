package com.cel.dataxfer.controller;

import com.cel.dataxfer.model.Users;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

public class User extends Controller{
	
	public static final int COOKIE_AGES = 86400;
	public static final int REMEMBER_COOKIE_AGES = 2592000;

	@ActionKey("/login")
	public void login(){
		String username = getCookie("username");
		setAttr("username", username);
		renderVelocity("login.html");
	}
	
	@ActionKey("/logon")
	public void logon(){
		String username = getPara("username");
		String password = getPara("password");
		String remember = getPara("remember");
		setAttr("username", username);
		
		if(remember != null && remember.equals("on")){
			setCookie("username", username, REMEMBER_COOKIE_AGES);
		}
		Users users = Users.dao.findFirst("select 1 from DataXfer_users where username=? and password=?", username, password);		
		if(users == null){
			setAttr("invalidLogin", "User name or password invalid.");
			renderVelocity("login.html");
			return;
		}
		
		setCookie("UID", username, COOKIE_AGES);
		redirect("/admin");
	}
	
	@ActionKey("/logout")
	public void logout(){
		this.setCookie("UID", null, -1);
		redirect("/login");
	}
}
