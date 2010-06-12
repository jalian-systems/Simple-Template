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

import com.jaliansystems.simpletemplate.templates.IfTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class IfElseTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createIfTemplate(t, lexer);
	}

	private static TemplateElement createIfTemplate(Token tokenGot, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token t = lexer.expect1(TokenType.getExtractableTokens(), ExpressionExtractor.getStartTokens());
		TemplateElement condition;
		if (ExpressionExtractor.isStartToken(t)) {
			condition = new ExpressionExtractor().extract(t, lexer);
		} else {
			condition = t.extract();
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