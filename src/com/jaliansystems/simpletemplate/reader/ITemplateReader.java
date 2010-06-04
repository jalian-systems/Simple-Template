package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public interface ITemplateReader {

	public abstract TemplateElement readTemplate() throws IOException, LexerException, ParserException;

}