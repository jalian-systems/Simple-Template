package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public abstract class AbstractLexer implements ILexer {

	private Token laToken;
	protected final LexerReader reader;

	public AbstractLexer(LexerReader in) {
		this.reader = in;
	}

	protected abstract Token getNextToken() throws IOException, LexerException;

	public final Token nextToken() throws IOException, LexerException {
		Token token;
		if (laToken == null)
			token = getNextToken();
		else
			token = laToken;
		laToken = null;
		return token;
	}

	public final Token lookAhead() throws IOException, LexerException {
		if (laToken != null)
			return laToken;
		reader.startPushback();
		laToken = getNextToken();
		return laToken;
	}

	@Override
	public void pushback() throws IOException {
		if (laToken != null) {
			reader.pushback();
			laToken = null ;
		}
	}
}
