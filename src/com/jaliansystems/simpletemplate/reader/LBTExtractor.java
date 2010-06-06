package com.jaliansystems.simpletemplate.reader;

import com.jaliansystems.simpletemplate.templates.LiteralBooleanTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class LBTExtractor implements ITemplateExtractor {
	public TemplateElement extract(Token t, ILexer lexer) {
		return new LiteralBooleanTemplate(
				t.getType() == TokenType.TT_TRUE, t.getFileName(), t.getLineNumber());
	}
}