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

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class BTExtractor implements ITemplateExtractor {

	public BTExtractor() {
	}

	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createBlockTemplate(t, lexer);
	}

	private TemplateElement createBlockTemplate(Token t,
			ILexer lexer) throws IOException,
			LexerException, ParserException {
		CompositeTemplate ct = new CompositeTemplate(t.getFileName(),
				t.getLineNumber());
		Token token;
		while ((token = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_BLOCK_END)).getType() != TokenType.TT_BLOCK_END) {
			ct.add(token.extract());
		}
		return ct;
	}

}