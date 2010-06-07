package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertEquals;

public class LexerTestSuper {

	public void assertToken(String expectedText, TokenType expectedType, Token actual) {
		assertEquals("Token types should match", expectedType, actual.getType());
		if (expectedText != null) {
			assertEquals("Token text should match expected text", expectedText, actual.getValue());
		}
	}

	public void assertToken(Token token, TokenType expectedTokenType, String expectedText,
			String expectedFilename, int expectedLineNumber) {
				assertEquals("Token should match", expectedTokenType, token.getType());
				if (expectedText != null) {
					assertEquals("Token text should match", expectedText,
							token.getValue());
				}
				if (expectedLineNumber != -1)
					assertEquals("Token linenumber should match", expectedLineNumber,
							token.getLineNumber());
				if (expectedFilename != null)
					assertEquals("Token filaname should match", expectedFilename,
							token.getFileName());
			}

}