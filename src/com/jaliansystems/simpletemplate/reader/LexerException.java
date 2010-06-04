package com.jaliansystems.simpletemplate.reader;

public class LexerException extends Exception {
	public LexerException(String fileName, int lineNumber, String message) {
		super(fileName + ":" + lineNumber + ": error: " + message);
	}

	private static final long serialVersionUID = 1L;

}
