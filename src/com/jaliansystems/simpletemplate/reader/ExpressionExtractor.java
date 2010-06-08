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

import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;

public class ExpressionExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		return createExpression(t, lexer);
	}

	private TemplateElement createExpression(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		VariableTemplate vt = new VariableTemplate(t.getValue(),
				t.getFileName(), t.getLineNumber());
		Token la = lexer.expect1r0(TokenType.TT_OPEN_BR);
		if (la != null) {
			return createIndexedExpression(la, vt, lexer);
		}
		return vt;
	}

	private TemplateElement createIndexedExpression(Token t,
			TemplateElement vt, ILexer lexer) throws LexerException,
			IOException, ParserException {
		while (t != null
				&& (t.getType() == TokenType.TT_OPEN_BR || t.getType() == TokenType.TT_NAME_SEPARATOR)) {
			if (t.getType() == TokenType.TT_OPEN_BR) {
				Token nextToken = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_IDENTIFIER);
				TemplateElement index;
				if (nextToken.getType() == TokenType.TT_IDENTIFIER) {
					index = createExpression(nextToken, lexer);
				} else {
					index = nextToken.extract();
				}
				vt = new IndexedAccessTemplate(vt, index, vt.getFileName(),
						vt.getLineNumber());
				lexer.expect1(TokenType.TT_CLOSE_BR);
			} else {
				Token id = lexer.expect1(TokenType.TT_IDENTIFIER);
				vt = new ObjectScopeTemplate(vt, id.getValue(),
						vt.getFileName(), vt.getLineNumber());
			}
			t = lexer.expect1r0(TokenType.TT_OPEN_BR,
					TokenType.TT_NAME_SEPARATOR);
		}
		return vt;
	}

}
