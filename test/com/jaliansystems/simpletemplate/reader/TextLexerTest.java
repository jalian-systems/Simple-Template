package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TextLexerTest extends LexerTestSuper {

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("$set $hello $java.lang.object $java$lang }$ } }\\$ }xy$lang}x");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
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
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		assertToken("set", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testBlockStartIsHandled() throws IOException, LexerException {
		StringReader reader = new StringReader("${ $xyz");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		assertToken(null, TokenType.TT_BLOCK_START, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("xyz", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testEscapeDoesntPassInvalidIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("$\\set..with");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		assertNull(lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithDotAtEndThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java.");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithTwoDotsInBetweenThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java..lang");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesStartingWithADotThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$.javlang");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}

	@Test
	public void testReadingAnIndexShouldReturnJustText() throws IOException, LexerException {
		StringReader reader = new StringReader("$greeting[1]$");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		assertToken("greeting", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("[1]", TokenType.TT_TEXT, lexer.nextToken());
	}
	
	@Test
	public void testEachLineIsATextToken() throws Exception {
		StringReader reader = new StringReader("line1\nline2\nline3\n");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "$", "$"));
		assertToken("line1\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("line2\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("line3\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());

	}
}
