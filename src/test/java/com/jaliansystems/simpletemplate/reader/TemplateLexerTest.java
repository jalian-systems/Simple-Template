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

import com.jaliansystems.simpletemplate.internal.reader.LexerException;
import com.jaliansystems.simpletemplate.internal.reader.LexerReader;
import com.jaliansystems.simpletemplate.internal.reader.TemplateLexer;
import com.jaliansystems.simpletemplate.internal.reader.TokenType;

public class TemplateLexerTest extends LexerTestSuper {

	@Test
	public void testReturnsProperTokens() throws IOException, LexerException {
		StringReader reader = new StringReader("$set $\\with $hello $java.lang.object $java $lang }$$lang { []()," +
				"if ifelse with set include $if $ifelse $with $set $include");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
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
		assertToken(null, TokenType.TT_OPEN_PAREN, lexer.nextToken());
		assertToken(null, TokenType.TT_CLOSE_PAREN, lexer.nextToken());
		assertToken(null, TokenType.TT_COMMA, lexer.nextToken());
		assertToken(null, TokenType.TT_IF, lexer.nextToken());
		assertToken(null, TokenType.TT_IFELSE, lexer.nextToken());
		assertToken(null, TokenType.TT_WITH, lexer.nextToken());
		assertToken(null, TokenType.TT_SET, lexer.nextToken());
		assertToken(null, TokenType.TT_INCLUDE, lexer.nextToken());
		assertToken(null, TokenType.TT_IF, lexer.nextToken());
		assertToken(null, TokenType.TT_IFELSE, lexer.nextToken());
		assertToken(null, TokenType.TT_WITH, lexer.nextToken());
		assertToken(null, TokenType.TT_SET, lexer.nextToken());
		assertToken(null, TokenType.TT_INCLUDE, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}

	@Test
	public void testScansStrings() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello World\"");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("Hello World", TokenType.TT_STRING, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testBackspaceInStringEscapesFollowingCharacters() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello \\\"\\nWorld\"");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("Hello \"\nWorld", TokenType.TT_STRING, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testReadsIntegers() throws IOException, LexerException {
		StringReader reader = new StringReader("\"Hello \\\"\\nWorld\" 1024 2321");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("Hello \"\nWorld", TokenType.TT_STRING, lexer.nextToken());
		assertToken("1024", TokenType.TT_INTEGER, lexer.nextToken());
		assertToken("2321", TokenType.TT_INTEGER, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
	
	@Test
	public void testReadsIdentifiers() throws IOException, LexerException {
		StringReader reader = new StringReader("java java.lang java.lang.Object true false to as");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
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
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
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
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));
		assertToken("java", TokenType.TT_IDENTIFIER, lexer.nextToken());
		assertToken("java.lang", TokenType.TT_START_IDENTIFIER, lexer.nextToken());
		assertToken(null, TokenType.TT_END_TEMPLATE, lexer.nextToken());
		assertToken(null, TokenType.TT_EOF, lexer.nextToken());
	}
}
