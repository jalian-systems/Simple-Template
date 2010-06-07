package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jaliansystems.simpletemplate.templates.MethodDefinitionTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class SubtemplateExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		String name = t.getValue();
		List<String> params = extractParams(lexer); 
		Token token = lexer.expect1(TokenType.getExtractableTokens());
		TemplateElement template = token.extract();
		return new MethodDefinitionTemplate(name, params, template, t.getFileName(), t.getLineNumber());
	}

	private List<String> extractParams(ILexer lexer) throws IOException, LexerException, ParserException {
		ArrayList<String> params = new ArrayList<String>();

		Token t = lexer.expect1(TokenType.TT_ALIAS, TokenType.TT_CLOSE_PAREN);
		while (t.getType() != TokenType.TT_CLOSE_PAREN) {
			params.add(t.getValue());
			t = lexer.expect1(TokenType.TT_COMMA, TokenType.TT_CLOSE_PAREN);
			if (t.getType() == TokenType.TT_COMMA)
				t = lexer.expect1(TokenType.TT_ALIAS);
		}
		return params;
	}

}
