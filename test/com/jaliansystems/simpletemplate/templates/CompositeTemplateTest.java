package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;


public class CompositeTemplateTest {

	@Test
	public void testCompositeTemplate() {
		CompositeTemplate ct = new CompositeTemplate(null, 0);
		ct.add(new LiteralTextTemplate("This is a composite template with ", null, 0));
		ct.add(new LiteralIntegerTemplate(3, null, 0));
		ct.add(new LiteralTextTemplate(" elements", null, 0));
		String result = ct.apply(new Scope());
		assertEquals("Composite element just concatenates its elements", "This is a composite template with 3 elements", result);
	}
}
