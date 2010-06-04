package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TemplateLexerTest {

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("$set $\\with $hello $java.lang.object $java $lang }$$lang { []");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken(null, TokenType.TT_SET, lexer.nextToken());
		assertToken("with", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("hello", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang.object", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("java", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_BLOCK_END, lexer.nextToken());
		assertToken("lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_CBLOCK_START, lexer.nextToken());
		assertToken(null, TokenType.TT_OPEN_BR, lexer.nextToken());
		assertToken(null, TokenType.TT_CLOSE_BR, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testScansStrings() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello World\"");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("Hello World", TokenType.TT_STRING, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testBackspaceInStringEscapesFollowingCharacters() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello \\\"\\nWorld\"");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("Hello \"\nWorld", TokenType.TT_STRING, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testReadsIntegers() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello \\\"\\nWorld\" 1024 2321");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("Hello \"\nWorld", TokenType.TT_STRING, lexer.nextToken());
		assertToken("1024", TokenType.TT_INTEGER, lexer.nextToken());
		assertToken("2321", TokenType.TT_INTEGER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testReadsIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("java java.lang java.lang.Object true false to as");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("java", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang.Object", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_TRUE, lexer.nextToken());
		assertToken(null, TokenType.TT_FALSE, lexer.nextToken());
		assertToken(null, TokenType.TT_TO, lexer.nextToken());
		assertToken(null, TokenType.TT_AS, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testEscapingKeywords() throws IOException, LexerException {
		StringReader reader = new StringReader("java java.lang java.lang.Object true false \\to \\as");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("java", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang.Object", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_TRUE, lexer.nextToken());
		assertToken(null, TokenType.TT_FALSE, lexer.nextToken());
		assertToken("to", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("as", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testReadingDifferentTypesOfIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("java $java.lang$");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));
		assertToken("java", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_START, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	private void assertToken(String expectedText, TokenType expectedType, Token actual) {
		if (expectedText != null) {
			assertEquals("Token text should match expected text", expectedText, actual.getValue());
		}
		assertEquals("Token types should match", expectedType, actual.getType());
	}
}
