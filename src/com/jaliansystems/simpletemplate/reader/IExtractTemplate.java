package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public interface IExtractTemplate {
	public TemplateElement extract(Token t) throws IOException, LexerException, ParserException;
}
