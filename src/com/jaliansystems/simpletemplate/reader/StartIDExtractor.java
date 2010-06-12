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

import com.jaliansystems.simpletemplate.templates.LoopTemplate;
import com.jaliansystems.simpletemplate.templates.MethodCallTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class StartIDExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		return createStartIdTemplate(t, lexer);
	}

	private TemplateElement createStartIdTemplate(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		TemplateElement ite;
		if (t.getType() == TokenType.TT_START_IDENTIFIER && isSimpleName(t)) {
			Token token = lexer.expect1r0(TokenType.TT_OPEN_PAREN);
			if (token != null)
				return new SubtemplateExtractor().extract(t, lexer);
		}
		TemplateElement vt = new ExpressionExtractor().extract(t, lexer);
		Token nextToken = lexer.expect1(TokenType.getExtractableTokens(),
				TokenType.TT_END_TEMPLATE);
		if (nextToken.getType() == TokenType.TT_END_TEMPLATE) {
			ite = vt;
		} else if (nextToken.getType() == TokenType.TT_METHOD_CALL) {
			MethodCallTemplate mct = (MethodCallTemplate) nextToken.extract();
			mct.setVariable(vt);
			ite = mct ;
		} else {
			TemplateElement template = nextToken.extract();
			ite = new LoopTemplate(vt, template, t.getFileName(),
					t.getLineNumber());
		}
		return ite;
	}

	private boolean isSimpleName(Token t) {
		return !t.getValue().contains(".");
	}

}