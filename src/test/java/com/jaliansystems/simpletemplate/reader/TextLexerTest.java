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

import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TextLexerTest extends LexerTestSuper {

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("$set $hello $java.lang.object $java$lang }$ } }\\$ }xy$lang}x");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
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
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("set", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testBlockStartIsHandled() throws IOException, LexerException {
		StringReader reader = new StringReader("${ $xyz");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken(null, TokenType.TT_BLOCK_START, lexer.nextToken());
		assertToken(" ", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("xyz", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testEscapeDoesntPassInvalidIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("$\\set..with");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertNull(lexer.nextToken());
	}

	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithDotAtEndThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java.");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesWithTwoDotsInBetweenThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$java..lang");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidIdentifierNamesStartingWithADotThrowsExceptions() throws IOException, LexerException {
		StringReader reader = new StringReader("$.javlang");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		lexer.nextToken();
	}

	@Test
	public void testReadingAnIndexShouldReturnJustText() throws IOException, LexerException {
		StringReader reader = new StringReader("$greeting[1]$");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("greeting", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken("[1]", TokenType.TT_TEXT, lexer.nextToken());
	}
	
	@Test
	public void testEachLineIsATextToken() throws Exception {
		StringReader reader = new StringReader("line1\nline2\nline3\n");
		AbstractLexer lexer = new TextLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("line1\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("line2\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken("line3\n", TokenType.TT_TEXT, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());

	}
}
