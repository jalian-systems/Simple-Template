package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.VariableScopeTemplate;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;


public class VariableScopeTemplateTest {

	@Test
	public void testIntroducingAVariableGivesAShortHand() {
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("alias.line1", null, 0);
		VariableScopeTemplate vst = new VariableScopeTemplate(withVar, "alias", null, 0);
		
		CompositeTemplate ct = new CompositeTemplate(null, 0);
		ct.add(vst);
		ct.add(template);
		
		Address address = new Address();
		address.setLine1("This line1 of address");
		Person person = new Person();
		person.setAddress(address);
		Scope scope = new Scope();
		scope.put("person", person);
		
		String result = ct.apply(scope);
		assertEquals("Expecting line1 from address", "This line1 of address", result);
	}
}
