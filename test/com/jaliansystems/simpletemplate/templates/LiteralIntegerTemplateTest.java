package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;

public class LiteralIntegerTemplateTest {

	@Test
	public void testIntegerAlwaysReturnsItself() {
		assertEquals("Whatever we pass is what we get", Integer.valueOf(10),
				Integer.valueOf(new LiteralIntegerTemplate(10, null, 0)
						.apply(new Scope())));
		assertEquals("Whatever we pass is what we get",
				Integer.valueOf(Integer.MAX_VALUE),
				Integer.valueOf(new LiteralIntegerTemplate(Integer.MAX_VALUE, null, 0)
						.apply(new Scope())));
	}

	@Test
	public void testAsBinary() {
		assertEquals("A zero gets us false", false, new LiteralIntegerTemplate(
				0, null, 0).asBinary(new Scope()));
		assertEquals("Any other value gets true", true, new LiteralIntegerTemplate(
				20, null, 0).asBinary(new Scope()));
	}
}
