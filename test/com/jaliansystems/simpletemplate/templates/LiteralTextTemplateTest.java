package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;


public class LiteralTextTemplateTest {

	@Test
	public void textTemplateDoesNotDoSubstitutions() {
		String text = "A tempting $way$ for failing $test$ case";
		LiteralTextTemplate template = new LiteralTextTemplate(text);
		Scope scope = new Scope();
		scope.put("way", "This is my way");
		scope.put("test", "Of testing");
		String result = template.apply(scope);
		assertEquals("Expect literally what we passed", text, result);
	}

	@Test
	public void testAsBinary() {
		assertEquals("A true text is always true", true, new LiteralTextTemplate("true").asBinary(new Scope()));
		assertEquals("Anyother text is false", true, new LiteralTextTemplate("").asBinary(new Scope()));
		assertEquals("Anyother text is false", true, new LiteralTextTemplate("some text with true in it").asBinary(new Scope()));
	}
}
