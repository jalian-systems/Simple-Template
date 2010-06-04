package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TextLexerTest {

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("$set $hello $java.lang.object $java$lang }$ } }\\$ }xy$lang}x");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		assertToken(null, TokenType.TT_SET, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("hello", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("java.lang.object", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("java", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken(null, TokenType.TT_BLOCK_END, lexer.nextToken());
		assertToken(" } }$ }xy", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("}x", TokenType.TT_TEXT, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testEscapedKeywordsAreHandled() throws IOException, LexerException {
		StringReader reader = new StringReader("$\\set");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		assertToken("set", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testEscapeDoesntPassInvalidIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("$\\set..with");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		assertNull(lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithDotAtEndThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java.");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithTwoDotsInBetweenThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java..lang");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesStartingWithADotThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$.javlang");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		lexer.nextToken();
	}

	@Test
	public void testReadingAnIndexShouldReturnJustText() throws IOException, LexerException {
		StringReader reader = new StringReader("$greeting[1]$");
		TextLexer lexer = new TextLexer(new LexerReader(reader));
		assertToken("greeting", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("[1]", TokenType.TT_TEXT, lexer.nextToken());
	}
	private void assertToken(String expectedText, TokenType expectedType, Token actual) {
		if (expectedText != null) {
			assertEquals("Token text should match expected text", expectedText, actual.getValue());
		}
		assertEquals("Token types should match", expectedType.toString(), actual.getType().toString());
	}
}
