package com.jaliansystems.simpletemplate.reader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.Scope;


public class TemplateEvaluationTest extends TemplateTestSuper {
	
	@Test
	public void testEvaluatesMethodsWithoutPrevixes() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { new String("World"),
				new String("Universe") });
		templateAssert("Hello $greeting[1].length$", "Hello 8", scope,
				"The Input and Output should be same");
	}

}
