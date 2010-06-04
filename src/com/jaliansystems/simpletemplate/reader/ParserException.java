package com.jaliansystems.simpletemplate.reader;

public class ParserException extends Exception {
	private static final long serialVersionUID = 1L;

	public ParserException(String fileName, int lineNumber, String message) {
		super(fileName + ":" + lineNumber + ": error: " + message);
	}

}
