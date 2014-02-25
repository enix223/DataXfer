package com.cel.dataxfer.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		validateRequired("username", "usernameMsg", "Username can not be blank.");
		validateRequired("password", "passwordMsg", "Password can not be blank.");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderVelocity("admin/login.html");
	}

}
