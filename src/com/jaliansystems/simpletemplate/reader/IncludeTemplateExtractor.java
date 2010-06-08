package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.net.URL;

import com.jaliansystems.simpletemplate.EvaluationError;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

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
