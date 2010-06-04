package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public interface ILexer {

	public Token nextToken() throws IOException, LexerException;
	public Token lookAhead() throws IOException, LexerException ;
	public void pushback() throws IOException;
}
