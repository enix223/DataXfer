package com.cel.dataxfer.core;

import com.cel.dataxfer.controller.Admin;
import com.cel.dataxfer.controller.DataSourceController;
import com.cel.dataxfer.controller.LogController;
import com.cel.dataxfer.controller.Trigger;
import com.cel.dataxfer.controller.User;
import com.cel.dataxfer.controller.UserPackage;
import com.cel.dataxfer.model.Config;
import com.cel.dataxfer.model.DataSourceModel;
import com.cel.dataxfer.model.Users;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

public class AppConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setBaseViewPath("views");
		me.setViewType(ViewType.VELOCITY);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", Admin.class, "/admin");
		me.add("/admin", Admin.class);
		me.add("/user", User.class, "/admin");
		me.add("/trigger", Trigger.class, "/admin");
		me.add("/packages", UserPackage.class, "/packages");
		me.add("/datasource", DataSourceController.class);
		me.add("/log", LogController.class, "/admin");
	}

	@Override
	public void configPlugin(Plugins me) {
		//MSSQL
		DruidPlugin dPlugin = new DruidPlugin("jdbc:jtds:sqlserver://172.29.25.25:1433/J12_DBS_DATA", 
				"saleslink", 
				"saleslink@db",
				"net.sourceforge.jtds.jdbc.Driver");
		dPlugin.start();
		
		//SQLite3
		//String db_path = PathKit.getWebRootPath() + "/database/data.db";
		//DruidPlugin dPlugin = new DruidPlugin("jdbc:sqlite://" + db_path.replace("\\", "/"), null, null, "org.sqlite.JDBC");
		
		ActiveRecordPlugin ap = new ActiveRecordPlugin(dPlugin);
		ap.setDialect(new AnsiSqlDialect());
		ap.addMapping("DataXfer_config", Config.class);
		ap.addMapping("DataXfer_users", Users.class);
		ap.addMapping("DataXfer_DataSource", DataSourceModel.class);
		me.add(ap);
	}

	@Override
	public void configInterceptor(Interceptors me) {		
	}

	@Override
	public void configHandler(Handlers me) {		
	}

}
