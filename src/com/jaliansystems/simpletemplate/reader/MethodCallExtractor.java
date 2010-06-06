package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class MethodCallExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		lexer.expect1(TokenType.TT_OPEN_PAREN);
		String name = t.getValue();
		List<TemplateElement> params = extractParams(lexer);
		MethodCallTemplate methodCallTemplate = new MethodCallTemplate(name, params, t.getFileName(), t.getLineNumber());
		buildCallChain(methodCallTemplate, lexer);
		return methodCallTemplate;
	}

	private void buildCallChain(MethodCallTemplate methodCallTemplate,
			ILexer lexer) throws IOException, LexerException, ParserException {
		Token token = lexer.expect1(TokenType.TT_END_TEMPLATE, TokenType.TT_METHOD_CALL);
		MethodCallTemplate parent = methodCallTemplate;
		while (token.getType() == TokenType.TT_METHOD_CALL) {
			MethodCallTemplate child = extractWithoutChain(token, lexer);
			parent.setNext(child);
			parent = child ;
			token = lexer.expect1(TokenType.TT_END_TEMPLATE, TokenType.TT_METHOD_CALL);
		}
	}

	public MethodCallTemplate extractWithoutChain(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		lexer.expect1(TokenType.TT_OPEN_PAREN);
		String name = t.getValue();
		List<TemplateElement> params = extractParams(lexer);
		MethodCallTemplate methodCallTemplate = new MethodCallTemplate(name, params, t.getFileName(), t.getLineNumber());
		return methodCallTemplate;
	}

	private List<TemplateElement> extractParams(ILexer lexer) throws IOException, LexerException, ParserException {
		ArrayList<TemplateElement> params = new ArrayList<TemplateElement>();

		Token t = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_CLOSE_PAREN);
		while (t.getType() != TokenType.TT_CLOSE_PAREN) {
			params.add(t.extract());
			t = lexer.expect1(TokenType.TT_COMMA, TokenType.TT_CLOSE_PAREN);
			if (t.getType() == TokenType.TT_COMMA)
				t = lexer.expect1(TokenType.getExtractableTokens());
		}
		return params;
	}
}
