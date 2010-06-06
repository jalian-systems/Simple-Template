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