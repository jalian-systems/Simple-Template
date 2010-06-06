package com.jaliansystems.simpletemplate.reader;

import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public final class LITExtractor implements ITemplateExtractor {
	public TemplateElement extract(Token t, ILexer lexer) {
		return new LiteralIntegerTemplate(
				Integer.parseInt(t.getValue()), t.getFileName(), t.getLineNumber());
	}
}