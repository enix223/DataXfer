package com.cel.dataxfer.controller;

import com.cel.dataxfer.model.Config;
import com.cel.dataxfer.validator.AddConfigValidator;
import com.cel.dataxfer.validator.EditConfigValidator;
import com.jfinal.aop.Before;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.Page;

public class Admin extends AppController {

	public static final int PAGE_SIZE = 50;
	
	public void index(){
		int p = getParaToInt("p", 1);
		String msg = getPara("msg", null);
		if(StringKit.notBlank(msg)){
			msg = msg.replace("-", " ");
		}
		
		
		Page<Config> entries = Config.dao.paginate(p, PAGE_SIZE, "select *", "from config");
		setAttr("page", entries.getPageNumber());
		setAttr("prev_page", entries.getPageNumber() - 1);
		setAttr("next_page", entries.getPageNumber() + 1);
		setAttr("total_pages", entries.getTotalPage());
		setAttr("entries", entries.getList());
		setAttr("msg", msg);
		
		//for login users
		setAttr("username", getCookie("username"));
		setAttr("UID", getCookie("UID"));
		renderVelocity("admin.html");
	}
	
	public void add(){
		//for login users
		setAttr("username", getCookie("username"));
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "create");
		renderVelocity("add.html");
	}
	
	@Before(AddConfigValidator.class)
	public void create(){
		String source = getPara("source");
		String sql = getPara("sql");
		String target = getPara("target");
		String truncate = getPara("truncate");
		String process = getPara("process");
		String year = getPara("year", "*");
		String month = getPara("month", "*");
		String day = getPara("day", "*");
		String weekday = getPara("weekday", "*");
		String specify = getPara("specify", "");
		String pkg = getPara("pkg", "");
		
		Config entryConfig = new Config();
		entryConfig.set("source", source);
		entryConfig.set("sql", sql);
		entryConfig.set("target", target);
		entryConfig.set("truncate", truncate);
		entryConfig.set("process", process);
		entryConfig.set("year", year);
		entryConfig.set("month", month);
		entryConfig.set("day", day);
		entryConfig.set("weekday", weekday);
		entryConfig.set("specify", specify);
		entryConfig.set("pkg", pkg);
		
		String msg = "";
		if(entryConfig.save()){
			msg = "Save-successful.";
		} else {
			msg = "Save failed.";
		}
		
		redirect("/admin?msg=" + msg);
	}
	
	public void edit(){
		int id = getParaToInt("id", 0);
		if(id == 0){
			String msg = "";
			msg = "id-not-specify.";
			redirect("/admin?msg=" + msg);
			return;
		}
		
		Config entry = Config.dao.findById(id);		
		if(entry == null){
			String msg = "";
			msg = "Record-not-found.";
			redirect("/admin?msg=" + msg);
			return;
		}

		//for login users
		setAttr("username", getCookie("username"));
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "update");
		setAttr("entry", entry);
		renderVelocity("add.html");
	}
	
	@Before(EditConfigValidator.class)
	public void update(){
		int id = getParaToInt("id", 0);
		String source = getPara("source");
		String sql = getPara("sql");
		String target = getPara("target");
		String truncate = getPara("truncate");
		String process = getPara("process");
		String year = getPara("year", "*");
		String month = getPara("month", "*");
		String day = getPara("day", "*");
		String weekday = getPara("weekday", "*");
		String specify = getPara("specify", "");
		String pkg = getPara("pkg", "");
		
		Config entryConfig = Config.dao.findById(id);
		entryConfig.set("source", source);
		entryConfig.set("sql", sql);
		entryConfig.set("target", target);
		entryConfig.set("truncate", truncate);
		entryConfig.set("process", process);
		entryConfig.set("year", year);
		entryConfig.set("month", month);
		entryConfig.set("day", day);
		entryConfig.set("weekday", weekday);
		entryConfig.set("specify", specify);
		entryConfig.set("pkg", pkg);
		
		String msg = "";
		if(entryConfig.update()){
			msg = "Save-successful.";
		} else {
			msg = "Save-failed.";
		}
		
		redirect("/admin?msg=" + msg);
	}
	
	public void delete(){
		String ids = getPara("ids");
		if(StringKit.isBlank(ids)){
			String msg = "Delete-operation-failed-id-not-specify";
			redirect("/admin?msg=" + msg);
			return;
		}
		
		String msg = "";
		String[] idArr = ids.split(",");
		for(String id : idArr){
			Config.dao.deleteById(id);
			msg = "Delete-entry-successful.";			
		}

		redirect("/admin?msg=" + msg);
	}
	
	public void view(){
		int id = getParaToInt("id", 0);
		if(id == 0){
			String msg = "";
			msg = "id-not-specify.";
			redirect("/admin?msg=" + msg);
			return;
		}
		
		Config entry = Config.dao.findById(id);		
		if(entry == null){
			String msg = "";
			msg = "Record-not-found.";
			redirect("/admin?msg=" + msg);
			return;
		}

		//for login users
		setAttr("username", getCookie("username"));
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "update");
		setAttr("item", entry);
		renderVelocity("view.html");
	}
}
