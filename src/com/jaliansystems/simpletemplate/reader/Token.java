package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Token {
	private final TokenType	type ;
	private final String value;
	
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public Token(TokenType type) {
		this(type, null);
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

	public TemplateElement extract() throws IOException, LexerException, ParserException {
		return getType().extract(this);
	}
}
