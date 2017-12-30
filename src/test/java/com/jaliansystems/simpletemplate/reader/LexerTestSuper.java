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

import static org.junit.Assert.assertEquals;

import com.jaliansystems.simpletemplate.internal.reader.Token;
import com.jaliansystems.simpletemplate.internal.reader.TokenType;

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