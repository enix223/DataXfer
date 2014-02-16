package com.cel.dataxfer.core;

import com.cel.dataxfer.controller.Admin;
import com.cel.dataxfer.controller.Trigger;
import com.cel.dataxfer.controller.User;
import com.cel.dataxfer.controller.UserPackage;
import com.cel.dataxfer.model.Config;
import com.cel.dataxfer.model.Users;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
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
	}

	@Override
	public void configPlugin(Plugins me) {
		String db_path = PathKit.getWebRootPath() + "/database/data.db";
		DruidPlugin dPlugin = new DruidPlugin("jdbc:sqlite://" + db_path.replace("\\", "/"), null, null, "org.sqlite.JDBC");
		me.add(dPlugin);
		
		ActiveRecordPlugin ap = new ActiveRecordPlugin(dPlugin);
		ap.addMapping("config", Config.class);
		ap.addMapping("users", Users.class);
		me.add(ap);
	}

	@Override
	public void configInterceptor(Interceptors me) {		
	}

	@Override
	public void configHandler(Handlers me) {		
	}

}
