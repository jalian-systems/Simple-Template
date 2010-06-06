package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.WithTemplate;

public final class WithTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createWithTemplate(t, lexer);
	}

	private TemplateElement createWithTemplate(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token next = lexer.expect1(TokenType.TT_IDENTIFIER);
		TemplateElement withVar = new ExpressionExtractor().extract(
				next, lexer);
		next = lexer.expect1r0(TokenType.TT_AS);
		String alias = null;
		if (next != null) {
			next = lexer.expect1(TokenType.TT_ALIAS);
			alias = next.getValue();
		}
		next = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement template = next.extract();
		return new WithTemplate(withVar, alias, template, t
				.getFileName(), t.getLineNumber());
	}
}