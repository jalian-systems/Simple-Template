package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;


public class LexerWithExpressionSeparatorsTest extends LexerTestSuper {

	@Test
	public void testSpecifyingDifferentStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new StringReader("@some.name@"), "<stream>", "@");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
	}

	@Test
	public void testSpecifyingDifferentMultiCharStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new StringReader("@$some.name@$x"), "<stream>", "@$");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testSpecifyingDifferentMultiCharAndEndStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new StringReader("<<{<<some.name}>>"), "<stream>", "<<", ">>");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken(null, TokenType.TT_BLOCK_START, lexer.nextToken());
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_BLOCK_END, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("<<set <<hello <<java.lang.object <<java<<lang }>> } }\\>> }xy<<lang}x");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>", "<<", ">>"));
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
		assertToken(" } }>> }xy", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("}x", TokenType.TT_TEXT, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testEscapedKeywordsAreHandled() throws IOException, LexerException {
		StringReader reader = new StringReader("$\\set");
		AbstractLexer lexer = new TextLexer(new LexerReader(reader, "<stream>"));
		assertToken("set", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testReturnsProperTokensForTemplateLexer() throws IOException, LexerException {
		StringReader reader = new StringReader("<<set <<\\with <<hello <<java.lang.object <<java <<lang }>><<lang { [] java.lang.object");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader, "<stream>", "<<", ">>"));
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
		assertToken("java.lang.object", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testExpressionSeparators() throws IOException, LexerException {
		StringReader reader = new StringReader("<template:ifelse true <template:trueMessage> <template:falseMessage>");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader, "<stream>", "<template:", ">"));
		assertToken(null, TokenType.TT_IFELSE, lexer.nextToken());
		assertToken(null, TokenType.TT_TRUE, lexer.nextToken());
		assertToken("trueMessage", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_END_TEMPLATE, lexer.nextToken());
		assertToken("falseMessage", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_END_TEMPLATE, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
}
