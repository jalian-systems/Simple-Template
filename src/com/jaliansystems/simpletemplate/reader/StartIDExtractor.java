package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.LoopTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class StartIDExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createStartIdTemplate(t, lexer);
	}

	private TemplateElement createStartIdTemplate(Token t,
			ILexer lexer) throws IOException, LexerException,
			ParserException {
		TemplateElement ite;
		TemplateElement vt = new ExpressionExtractor().extract(t, lexer);
		Token nextToken = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_END_TEMPLATE);
		if (nextToken.getType() == TokenType.TT_END_TEMPLATE) {
			ite = vt;
		} else {
			TemplateElement template = nextToken.extract();
			ite = new LoopTemplate(vt, template, t.getFileName(),
					t.getLineNumber());
		}
		return ite;
	}

}