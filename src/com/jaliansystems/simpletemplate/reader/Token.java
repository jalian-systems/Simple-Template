package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Token {
	public static final Token TOK_WITH = new Token(TokenType.TT_WITH);
	public static final Token TOK_SET = new Token(TokenType.TT_SET);
	public static final Token TOK_IF = new Token(TokenType.TT_IF);
	public static final Token TOK_IFELSE = new Token(TokenType.TT_IFELSE);
	public static final Token TOK_BLOCK_START = new Token(TokenType.TT_BLOCK_START);
	public static final Token TOK_BLOCK_END = new Token(TokenType.TT_BLOCK_END);
	public static final Token TOK_OPEN_BR = new Token(TokenType.TT_OPEN_BR);
	public static final Token TOK_CLOSE_BR = new Token(TokenType.TT_CLOSE_BR);
	public static final Token TOK_TO = new Token(TokenType.TT_TO);
	public static final Token TOK_AS = new Token(TokenType.TT_AS);
	public static final Token TOK_TRUE = new Token(TokenType.TT_TRUE);
	public static final Token TOK_FALSE = new Token(TokenType.TT_FALSE);
	public static final Token TOK_CBLOCK_START = new Token(TokenType.TT_CBLOCK_START);
	public static final Token TOK_START = new Token(TokenType.TT_START);
	public static final Token TOK_NAME_SEPARATOR = new Token(TokenType.TT_NAME_SEPARATOR);
	public static final Token TOK_EOF = new Token(TokenType.TT_EOF);

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
