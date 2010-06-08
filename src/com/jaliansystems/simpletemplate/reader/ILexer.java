package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public interface ILexer {

	public Token lookAhead() throws IOException, LexerException;

	public void pushbackLACharacters() throws IOException;

	public Token expect1(TokenType... types) throws IOException,
			LexerException, ParserException;

	public Token expect1r0(TokenType... types) throws IOException,
			LexerException, ParserException;

	public Token expect1(TokenType[] types, TokenType... others) throws IOException,
			LexerException, ParserException;

	public Token expect1r0(TokenType[] types, TokenType... others) throws IOException,
			LexerException, ParserException;
}
