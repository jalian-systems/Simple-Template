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

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.WithTemplate;

public final class WithTExtractor implements ITemplateExtractor {
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createWithTemplate(t, lexer);
	}

	private TemplateElement createWithTemplate(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token next = lexer.expect1(ExpressionExtractor.getStartTokens());
		TemplateElement withVar = new ExpressionExtractor().extract(
				next, lexer);
		next = lexer.expect1r0(TokenType.TT_AS);
		String alias = null;
		if (next != null) {
			next = lexer.expect1(TokenType.TT_ALIAS);
			alias = next.getValue();
		}
		next = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement template = next.extract();
		return new WithTemplate(withVar, alias, template, t
				.getFileName(), t.getLineNumber());
	}
}