/*
 *   Copyright 2010 Jalian Systems Pvt. Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/

package com.jaliansystems.simpletemplate.reader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;


public class LexerWithExpressionSeparatorsTest extends LexerTestSuper {

	@Test
	public void testSpecifyingDifferentStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new File(".").toURI().toURL(), new StringReader("@some.name@"), "<stream>", "@", "@");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
	}

	@Test
	public void testSpecifyingDifferentMultiCharStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new File(".").toURI().toURL(), new StringReader("@$some.name@$x"), "<stream>", "@$", "@$");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testSpecifyingDifferentMultiCharAndEndStartToken() throws IOException, LexerException {
		LexerReader in = new LexerReader(new File(".").toURI().toURL(), new StringReader("<<{<<some.name}>>"), "<stream>", "<<", ">>");
		AbstractLexer lexer = new TextLexer(in);
		
		assertToken(null, TokenType.TT_BLOCK_START, lexer.nextToken());
		assertToken("some.name", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_BLOCK_END, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("<<set <<hello <<java.lang.object <<java<<lang }>> } }\\>> }xy<<lang}x");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "<<", ">>"));
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
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("set", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testReturnsProperTokensForTemplateLexer() throws IOException, LexerException {
		StringReader reader = new StringReader("<<set <<\\with <<hello <<java.lang.object <<java <<lang }>><<lang { [] java.lang.object");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "<<", ">>"));
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
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "<template:", ">"));
		assertToken(null, TokenType.TT_IFELSE, lexer.nextToken());
		assertToken(null, TokenType.TT_TRUE, lexer.nextToken());
		assertToken("trueMessage", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_END_TEMPLATE, lexer.nextToken());
		assertToken("falseMessage", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_END_TEMPLATE, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
}
