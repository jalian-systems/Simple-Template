package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableScopeTemplate;

public final class SetTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createSetTemplate(t, lexer);
	}

	private TemplateElement createSetTemplate(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token next = lexer.expect1(TokenType.TT_ALIAS);
		String alias = next.getValue();
		lexer.expect1(TokenType.TT_TO);
		next = lexer.expect1(TokenType.TT_IDENTIFIER);
		TemplateElement setVar = new ExpressionExtractor().extract(
				next, lexer);
		return new VariableScopeTemplate(setVar, alias,
				t.getFileName(), t.getLineNumber());
	}
}