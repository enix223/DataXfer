package com.cel.dataxfer.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;

public class Auth implements Interceptor{

	/*
	 * Check the user login or not.
	 */
	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		String uid = controller.getCookie("UID");
		if(StringKit.isBlank(uid)){
			controller.redirect("/login");
			return;
		}
				
		ai.invoke();
	}

}
