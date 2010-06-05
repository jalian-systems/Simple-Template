package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.util.Arrays;

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

	public Token expect1(TokenType... types) throws IOException,
			LexerException, ParserException {
		Token t = expect1r0(types);
		if (t == null) {
			t = nextToken();
			throw new ParserException(reader.getFileName(), reader.getLineNumber(),
					"Expecting one of " + Arrays.asList(types) + " Got: " + t);
		}
		return t;
	}

	public Token expect1r0(TokenType... types)
			throws IOException, LexerException {
		Token la = lookAhead();
		for (int i = 0; i < types.length; i++) {
			if (la.getType() == types[i]) {
				return nextToken();
			}
			if (types[i] == TokenType.TT_ALIAS
					&& la.getType() == TokenType.TT_IDENTIFIER) {
				String value = la.getValue();
				if (!value.contains(".")) {
					nextToken();
					return new Token(TokenType.TT_ALIAS, la.getValue());
				}
			}
		}
		return null;
	}
}
