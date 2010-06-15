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

import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.MethodCallTemplate;
import com.jaliansystems.simpletemplate.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;

public class ExpressionExtractor implements ITemplateExtractor {

	private static TokenType[] startTokens = new TokenType[] {
			TokenType.TT_IDENTIFIER, TokenType.TT_START_IDENTIFIER,
			TokenType.TT_METHOD_CALL, TokenType.TT_START_METHOD_CALL };

	private static List<TokenType> asList = new ArrayList<TokenType>(
			Arrays.asList(startTokens));

	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		ensureIsOneOfStartTokens(t);
		return createExpression(t, lexer);
	}

	private void ensureIsOneOfStartTokens(Token t) {
		if (isStartToken(t))
			return;
		throw new ParserException(t.getFileName(), t.getLineNumber(),
				"Expecting one of " + asList + " Got: " + t);
	}

	private TemplateElement createExpression(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		TemplateElement template;
		if (t.getType() == TokenType.TT_START_IDENTIFIER
				|| t.getType() == TokenType.TT_IDENTIFIER)
			template = new VariableTemplate(t.getValue(), t.getFileName(),
					t.getLineNumber());
		else
			template = new MethodCallExtractor().extract(t, lexer);
		return createChainedExpression(lexer, template);
	}

	private TemplateElement createChainedExpression(ILexer lexer,
			TemplateElement template) throws IOException {
		Token la = lexer.expect1r0(TokenType.TT_OPEN_BR,
				TokenType.TT_METHOD_CALL, TokenType.TT_NAME_SEPARATOR);
		if (la == null)
			return template;
		while (la != null) {
			if (la.getType() == TokenType.TT_METHOD_CALL)
				template = createMethodCall(la, template, lexer);
			else if (la.getType() == TokenType.TT_OPEN_BR)
				template = createIndexedExpression(la, template, lexer);
			else
				template = createObjectScope(la, template, lexer);
			la = lexer.expect1r0(TokenType.TT_OPEN_BR,
					TokenType.TT_METHOD_CALL, TokenType.TT_NAME_SEPARATOR);
		}
		return template;
	}

	private TemplateElement createObjectScope(Token la,
			TemplateElement template, ILexer lexer) throws LexerException, ParserException, IOException {
		Token nextToken = lexer.expect1(TokenType.TT_IDENTIFIER);
		return new ObjectScopeTemplate(template, nextToken.getValue(), nextToken.getFileName(), nextToken.getLineNumber());
	}

	private TemplateElement createMethodCall(Token la,
			TemplateElement template, ILexer lexer) throws LexerException,
			ParserException, IOException {
		TemplateElement extract = new MethodCallExtractor().extract(la, lexer);
		((MethodCallTemplate) extract).setVariable(template);
		return extract;
	}

	private TemplateElement createIndexedExpression(Token t,
			TemplateElement template, ILexer lexer) throws LexerException,
			IOException, ParserException {
		Token nextToken = lexer.expect1(TokenType.getExtractableTokens(),
				startTokens);
		TemplateElement index;
		if (nextToken.getType().isExtractable()) {
			index = nextToken.extract();
		} else {
			index = createExpression(nextToken, lexer);
		}
		template = new IndexedAccessTemplate(template, index,
				template.getFileName(), template.getLineNumber());
		lexer.expect1(TokenType.TT_CLOSE_BR);
		return template;
	}

	public static TokenType[] getStartTokens() {
		return startTokens;
	}

	public static boolean isStartToken(Token t) {
		return asList.contains(t.getType());
	}

}
