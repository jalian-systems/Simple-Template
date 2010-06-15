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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jaliansystems.simpletemplate.templates.MethodCallTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class MethodCallExtractor implements ITemplateExtractor {

	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		lexer.expect1(TokenType.TT_OPEN_PAREN);
		String name = t.getValue();
		List<TemplateElement> params = extractParams(lexer);
		MethodCallTemplate methodCallTemplate = new MethodCallTemplate(name,
				params, t.getFileName(), t.getLineNumber());
		buildCallChain(methodCallTemplate, lexer);
		return methodCallTemplate;
	}

	private void buildCallChain(MethodCallTemplate methodCallTemplate,
			ILexer lexer) throws IOException, LexerException, ParserException {
		Token token = lexer.expect1r0(TokenType.TT_METHOD_CALL);
		MethodCallTemplate parent = methodCallTemplate;
		while (token != null) {
			MethodCallTemplate child = extractWithoutChain(token, lexer);
			parent.setNext(child);
			parent = child;
			token = lexer.expect1r0(TokenType.TT_METHOD_CALL);
		}
	}

	public MethodCallTemplate extractWithoutChain(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		lexer.expect1(TokenType.TT_OPEN_PAREN);
		String name = t.getValue();
		List<TemplateElement> params = extractParams(lexer);
		MethodCallTemplate methodCallTemplate = new MethodCallTemplate(name,
				params, t.getFileName(), t.getLineNumber());
		return methodCallTemplate;
	}

	private List<TemplateElement> extractParams(ILexer lexer)
			throws IOException, LexerException, ParserException {
		ArrayList<TemplateElement> params = new ArrayList<TemplateElement>();

		TokenType[] startTokens = ExpressionExtractor.getStartTokens();
		TokenType[] others = Arrays.copyOf(startTokens, startTokens.length + 1);
		others[startTokens.length] = TokenType.TT_CLOSE_PAREN;
		Token t = lexer.expect1(TokenType.getExtractableTokens(), others);
		while (t.getType() != TokenType.TT_CLOSE_PAREN) {
			if (t.getType().isExtractable())
				params.add(t.extract());
			else
				params.add(new ExpressionExtractor().extract(t, lexer));
			t = lexer.expect1(new TokenType[] { TokenType.TT_COMMA }, others);
			if (t.getType() == TokenType.TT_COMMA)
				t = lexer.expect1(TokenType.getExtractableTokens(),
						ExpressionExtractor.getStartTokens());
		}
		return params;
	}
}
