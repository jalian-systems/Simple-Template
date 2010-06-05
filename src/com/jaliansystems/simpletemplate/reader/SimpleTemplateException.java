package com.jaliansystems.simpletemplate.reader;

public class SimpleTemplateException extends Exception {
	private static final long serialVersionUID = 1L;

	public SimpleTemplateException(String message) {
		super(message);
		setStackTrace(new StackTraceElement[0]);
	}
}
