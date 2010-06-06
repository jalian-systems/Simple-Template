package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.IfTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class IfElseTExtractor implements ITemplateExtractor {
	@Override
	public TemplateElement extract(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		return createIfTemplate(t, lexer);
	}

	private static TemplateElement createIfTemplate(Token tokenGot, ILexer lexer)
			throws IOException, LexerException, ParserException {
		Token t = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_IDENTIFIER);
		TemplateElement condition;
		if (t.getType() == TokenType.TT_IDENTIFIER) {
			condition = new ExpressionExtractor().extract(t, lexer);
		} else {
			condition = t.extract();
		}
		t = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement trueBranch = t.extract();
		TemplateElement falseBranch = null;
		if (tokenGot.getType() == TokenType.TT_IFELSE) {
			t = lexer.expect1(TokenType.getExtractableTokens());
			falseBranch = t.extract();
		}
		return new IfTemplate(condition, trueBranch, falseBranch,
				tokenGot.getFileName(), tokenGot.getLineNumber());
	}

}