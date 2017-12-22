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
import com.jaliansystems.simpletemplate.templates.VariableScopeTemplate;

public final class SetTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		return createSetTemplate(t, lexer);
	}

	private TemplateElement createSetTemplate(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token next = lexer.expect1(TokenType.TT_ALIAS);
		String alias = next.getValue();
		lexer.expect1(TokenType.TT_TO);
		next = lexer.expect1(TokenType.getExtractableTokens(),
				ExpressionExtractor.getStartTokens());
		TemplateElement setVar;
		if (next.getType().isExtractable())
			setVar = next.extract();
		else
			setVar = new ExpressionExtractor().extract(next, lexer);
		return new VariableScopeTemplate(setVar, alias, t.getFileName(),
				t.getLineNumber());
	}
}