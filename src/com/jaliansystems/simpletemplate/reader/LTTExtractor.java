package com.jaliansystems.simpletemplate.reader;

import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class LTTExtractor implements ITemplateExtractor {
	public TemplateElement extract(Token t, ILexer lexer) {
		return new LiteralTextTemplate(t.getValue(), t.getFileName(), t.getLineNumber());
	}
}