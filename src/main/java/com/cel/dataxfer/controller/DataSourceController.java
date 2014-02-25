package com.cel.dataxfer.controller;

import com.cel.dataxfer.model.DataSourceModel;
import com.cel.dataxfer.validator.AddDSValidator;
import com.cel.dataxfer.validator.EditDSValidator;
import com.jfinal.aop.Before;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.Page;

public class DataSourceController extends AppController {

	public static final int PAGE_SIZE = 50;
	
	public void index(){
		int p = getParaToInt("p", 1);
		String msg = getPara("msg", null);
		if(StringKit.notBlank(msg)){
			msg = msg.replace("-", " ");
		}
		
		
		Page<DataSourceModel> entries = DataSourceModel.dao.paginate(p, PAGE_SIZE, "select *", "from DataXfer_datasource");
		setAttr("page", entries.getPageNumber());
		setAttr("prev_page", entries.getPageNumber() - 1);
		setAttr("next_page", entries.getPageNumber() + 1);
		setAttr("total_pages", entries.getTotalPage());
		setAttr("entries", entries.getList());
		setAttr("msg", msg);
		
		//for login users
		setAttr("UID", getCookie("UID"));
		renderVelocity("index.html");
	}
	
	public void add(){
		//for login users
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "create");
		renderVelocity("add.html");
	}
	
	@Before(AddDSValidator.class)
	public void create(){
		String source = getPara("dsname");
		String driver = getPara("driver");
		String url = getPara("url");
		String username = getPara("username");
		String passwd = getPara("passwd");

		DataSourceModel entry = new DataSourceModel();
		entry.set("dsname", source);
		entry.set("driver", driver);
		entry.set("url", url);
		entry.set("username", username);
		entry.set("passwd", passwd);
		
		String msg = "";
		if(entry.save()){
			msg = "Save-successful.";
		} else {
			msg = "Save failed.";
		}
		
		redirect("/datasource?msg=" + msg);
	}
	
	public void edit(){
		int id = getParaToInt("id", 0);
		if(id == 0){
			String msg = "";
			msg = "id-not-specify.";
			redirect("/datasource?msg=" + msg);
			return;
		}
		
		DataSourceModel entry = DataSourceModel.dao.findById(id);		
		if(entry == null){
			String msg = "";
			msg = "Record-not-found.";
			redirect("/datasource?msg=" + msg);
			return;
		}

		//for login users
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "update");
		setAttr("entry", entry);
		renderVelocity("add.html");
	}
	
	@Before(EditDSValidator.class)
	public void update(){
		int id = getParaToInt("id", 0);
		String source = getPara("dsname");
		String driver = getPara("driver");
		String url = getPara("url");
		String username = getPara("username");
		String passwd = getPara("passwd");
		
		DataSourceModel entry = DataSourceModel.dao.findById(id);
		entry.set("dsname", source);
		entry.set("driver", driver);
		entry.set("url", url);
		entry.set("username", username);
		entry.set("passwd", passwd);
		
		String msg = "";
		if(entry.update()){
			msg = "Save-successful.";
		} else {
			msg = "Save-failed.";
		}
		
		redirect("/datasource?msg=" + msg);
	}
	
	public void delete(){
		String ids = getPara("ids");
		if(StringKit.isBlank(ids)){
			String msg = "Delete-operation-failed-id-not-specify";
			redirect("/datasource?msg=" + msg);
			return;
		}
		
		String msg = "";
		String[] idArr = ids.split(",");
		for(String id : idArr){
			DataSourceModel.dao.deleteById(id);
			msg = "Delete-entry-successful.";			
		}

		redirect("/datasource?msg=" + msg);
	}
	
	public void view(){
		int id = getParaToInt("id", 0);
		if(id == 0){
			String msg = "";
			msg = "id-not-specify.";
			redirect("/datasource?msg=" + msg);
			return;
		}
		
		DataSourceModel entry = DataSourceModel.dao.findById(id);		
		if(entry == null){
			String msg = "";
			msg = "Record-not-found.";
			redirect("/datasource?msg=" + msg);
			return;
		}

		//for login users
		setAttr("UID", getCookie("UID"));
		
		setAttr("action", "update");
		setAttr("item", entry);
		renderVelocity("view.html");
	}
}
