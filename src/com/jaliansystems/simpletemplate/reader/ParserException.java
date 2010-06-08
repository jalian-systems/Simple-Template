package com.jaliansystems.simpletemplate.reader;

import com.jaliansystems.simpletemplate.EvaluationError;

public class ParserException extends EvaluationError {
	private static final long serialVersionUID = 1L;

	public ParserException(String fileName, int lineNumber, String message) {
		super(fileName + ":" + lineNumber + ": error: " + message);
	}

	public ParserException(String message) {
		super(message);
	}

}
