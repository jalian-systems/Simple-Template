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
import java.net.URL;

import com.jaliansystems.simpletemplate.TemplateReader;
import com.jaliansystems.simpletemplate.internal.templates.EvaluationError;
import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;

public class IncludeTemplateExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException, LexerException, ParserException {
		Token token = lexer.expect1(TokenType.TT_STRING);
		URL url = new URL(lexer.getReader().getContextURL(), token.getValue());
		try {
			TemplateReader reader = new TemplateReader(url, lexer.getStartToken(), lexer.getEndToken());
			return reader.readTemplate();
		} catch (Exception e) {
			if (e instanceof LexerException || e instanceof ParserException) {
				StringBuffer message = new StringBuffer(e.getMessage());
				message.append("\n     in file included at " + t.getFileName() + ":" + t.getLineNumber());
				throw new EvaluationError(message.toString());
			}
		}
		return null;
	}
}
