package com.jaliansystems.simpletemplate;

public class EvaluationError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EvaluationError(String message) {
		super(message);
		setStackTrace(new StackTraceElement[0]);
	}
}
