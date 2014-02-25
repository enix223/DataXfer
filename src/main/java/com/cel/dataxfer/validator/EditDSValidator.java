package com.cel.dataxfer.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class EditDSValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateRequired("id", "idMsg", "Object id is missing.");
		validateRequiredString("dsname", "sourceMsg", "Data source name could not be blank");
		validateRequiredString("driver", "driverMsg", "Driver could not be blank");
		validateRequiredString("url", "urlMsg", "JDBC URL could not be blank");
		validateRequiredString("username", "userMsg", "JDBC connection user name could not be blank");
		validateRequiredString("passwd", "passwdMsg", "JDBC connection password could not be blank");	
	}

	@Override
	protected void handleError(Controller c) {
		c.renderVelocity("add.html");		
	}

}
