package com.jaliansystems.simpletemplate.reader;

import com.jaliansystems.simpletemplate.EvaluationError;

public class LexerException extends EvaluationError {
	public LexerException(String fileName, int lineNumber, String message) {
		super(fileName + ":" + lineNumber + ": error: " + message);
	}

	private static final long serialVersionUID = 1L;

}
