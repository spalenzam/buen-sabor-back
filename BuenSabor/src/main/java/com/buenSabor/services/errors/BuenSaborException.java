package com.buenSabor.services.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BuenSaborException extends AbstractThrowableProblem{

	private static final long serialVersionUID = 1L;
	
	private final String entityName;
	
	private final String errorKey;

	public BuenSaborException(String title, String detail) {
		super(ErrorConstants.DEFAULT_TYPE, title, Status.BAD_REQUEST, detail);
		this.entityName = title;
		this.errorKey = detail;
	}
	
	
}
