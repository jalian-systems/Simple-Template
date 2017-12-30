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

package com.jaliansystems.simpletemplate.internal.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.internal.templates.IfTemplate;
import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;

public final class IfElseTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createIfTemplate(t, lexer);
	}

	private TemplateElement createIfTemplate(Token tokenGot, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token t = lexer.expect1(TokenType.getExtractableTokens(), ExpressionExtractor.getStartTokens());
		TemplateElement condition;
		if (t.getType().isExtractable()) {
			condition = t.extract();
		} else {
			condition = new ExpressionExtractor().extract(t, lexer);
		}
		t = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement trueBranch = t.extract();
		TemplateElement falseBranch = null;
		if (tokenGot.getType() == TokenType.TT_IFELSE) {
			t = lexer.expect1(TokenType.getExtractableTokens());
			falseBranch = t.extract();
		}
		return new IfTemplate(condition, trueBranch, falseBranch,
				tokenGot.getFileName(), tokenGot.getLineNumber());
	}

}