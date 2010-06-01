package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.IfTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralBooleanTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;

public class IfTemplateTest {

	@Test
	public void testIfTemplateWithSimpleCondition() {
		LiteralTextTemplate trueBranch = new LiteralTextTemplate("This is true");
		LiteralTextTemplate falseBranch = new LiteralTextTemplate(
				"This is false");

		assertEquals("IfTemplate should return the true one", "This is true",
				new IfTemplate(new LiteralBooleanTemplate(true), trueBranch,
						falseBranch).apply(new Scope()));
		assertEquals("IfTemplate should return the false one", "This is false",
				new IfTemplate(new LiteralBooleanTemplate(false), trueBranch,
						falseBranch).apply(new Scope()));
	}
}
