package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Token {
	private final TokenType	type ;
	private final String value;
	private final int lineNumber;
	private final String fileName;
	
	public Token(TokenType type, String value, String fileName, int lineNumber) {
		this.type = type;
		this.value = value;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}
	
	public Token(TokenType type, String fileName, int lineNumber) {
		this(type, null, fileName, lineNumber);
	}

	@Override
	public String toString() {
		return type.toString() + (value != null ? "(" + value + ")" : "");
	}

	public String getValue() {
		return value;
	}

	public TokenType getType() {
		return type;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public TemplateElement extract() throws IOException, LexerException, ParserException {
		return getType().extract(this);
	}
}
