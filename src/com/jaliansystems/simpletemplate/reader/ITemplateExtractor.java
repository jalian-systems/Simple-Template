package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public interface ITemplateExtractor {
	public TemplateElement extract(Token t, ILexer lexer) throws IOException, LexerException, ParserException;
}
