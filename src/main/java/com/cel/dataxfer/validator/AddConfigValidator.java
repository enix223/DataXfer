package com.cel.dataxfer.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class AddConfigValidator extends Validator{

	@Override
	protected void validate(Controller c) {		
		validateRequiredString("source", "sourceMsg", "Source table could not be blank");
		validateRequiredString("sql", "sqlMsg", "Source select SQL could not be blank");
		validateRequiredString("target", "targetMsg", "Target table could not be blank");
		validateRequiredString("truncate", "truncateMsg", "Truncate flag could not be blank");
		validateRequiredString("process", "processMsg", "Process flag could not be blank");		
	}

	@Override
	protected void handleError(Controller c) {
		c.renderVelocity("add.html");
	}

}
