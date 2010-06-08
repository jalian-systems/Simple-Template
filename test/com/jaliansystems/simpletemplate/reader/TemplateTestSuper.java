package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class TemplateTestSuper {

	public TemplateTestSuper() {
		super();
	}

	protected void templateAssert(String templateText, String expected, Scope scope,
			String message) throws Exception {
		templateAssert(templateText, expected, scope, message, null, null);
			}

	protected void templateAssert(String templateText, String expected, Scope scope,
			String message, String start, String end) throws Exception {
				Reader in = new StringReader(templateText);
				ITemplateReader reader = new TemplateReader(in, "<stream>", start, end);
				TemplateElement template = reader.readTemplate();
				String result = template.apply(scope);
				assertEquals(message, expected, result);
			}

}