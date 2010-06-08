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

public class TokenTest extends LexerTestSuper {

	@Test
	public void testCarriesLineNumberInformation() throws IOException,
			LexerException, ParserException {
		StringReader reader = new StringReader(
				"${   {   }$\nsecond.line \"at other position\"\nthird.line");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(new File(".").toURI().toURL(), reader, "<stream>", "$", "$"));

		assertToken(lexer.expect1(TokenType.TT_BLOCK_START),
				TokenType.TT_BLOCK_START, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_CBLOCK_START),
				TokenType.TT_CBLOCK_START, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_BLOCK_END),
				TokenType.TT_BLOCK_END, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_IDENTIFIER),
				TokenType.TT_IDENTIFIER, "second.line", "<stream>", 2);
		assertToken(lexer.expect1(TokenType.TT_STRING), TokenType.TT_STRING,
				"at other position", "<stream>", 2);
		assertToken(lexer.expect1(TokenType.TT_IDENTIFIER),
				TokenType.TT_IDENTIFIER, "third.line", "<stream>", 3);
	}

}
