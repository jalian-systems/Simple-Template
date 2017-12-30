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
import java.util.ArrayList;
import java.util.List;

import com.jaliansystems.simpletemplate.internal.templates.MethodDefinitionTemplate;
import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;

public class SubtemplateExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		String name = t.getValue();
		List<String> params = extractParams(lexer); 
		Token token = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement template = token.extract();
		return new MethodDefinitionTemplate(name, params, template, t.getFileName(), t.getLineNumber());
	}

	private List<String> extractParams(ILexer lexer) throws IOException, LexerException, ParserException {
		ArrayList<String> params = new ArrayList<String>();

		Token t = lexer.expect1(TokenType.TT_ALIAS, TokenType.TT_CLOSE_PAREN);
		while (t.getType() != TokenType.TT_CLOSE_PAREN) {
			params.add(t.getValue());
			t = lexer.expect1(TokenType.TT_COMMA, TokenType.TT_CLOSE_PAREN);
			if (t.getType() == TokenType.TT_COMMA)
				t = lexer.expect1(TokenType.TT_ALIAS);
		}
		return params;
	}

}
