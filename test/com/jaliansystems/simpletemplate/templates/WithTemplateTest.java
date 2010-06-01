package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;
import com.jaliansystems.simpletemplate.templates.WithTemplate;


public class WithTemplateTest {

	@Test
	public void testGetsResolvesInLocalScope() {
		VariableTemplate withVar = new VariableTemplate("person.address");
		VariableTemplate template = new VariableTemplate("line1");
		WithTemplate withTemplate = new WithTemplate(withVar, template);
		Address address = new Address();
		address.setLine1("This line1 of address");
		Person person = new Person();
		person.setAddress(address);
		Scope scope = new Scope();
		scope.put("person", person);
		String result = withTemplate.apply(scope);
		assertEquals("Expecting line1 from address", "This line1 of address", result);
	}

	@Test
	public void testParentScopeIsHidden() {
		VariableTemplate withVar = new VariableTemplate("person.address");
		VariableTemplate template = new VariableTemplate("line1");
		WithTemplate withTemplate = new WithTemplate(withVar, template);
		Address address = new Address();
		address.setLine1("This line1 of address");
		Person person = new Person();
		person.setAddress(address);
		Scope scope = new Scope();
		scope.put("person", person);
		scope.put("line1", "This is line1 of address from parent scope");
		String result = withTemplate.apply(scope);
		assertEquals("Expecting line1 from address", "This line1 of address", result);
	}

	@Test
	public void testParentScopeIsUsedWhenVarsStartWith_() {
		VariableTemplate withVar = new VariableTemplate("person.address");
		VariableTemplate template = new VariableTemplate("_line1");
		WithTemplate withTemplate = new WithTemplate(withVar, template);
		Address address = new Address();
		address.setLine1("This line1 of address");
		Person person = new Person();
		person.setAddress(address);
		Scope scope = new Scope();
		scope.put("person", person);
		scope.put("line1", "This is line1 of address from parent scope");
		String result = withTemplate.apply(scope);
		assertEquals("Expecting line1 from address", "This is line1 of address from parent scope", result);
	}

	@Test
	public void testGetsResolvesInLocalScopeWithAVariableName() {
		VariableTemplate withVar = new VariableTemplate("person.address");
		VariableTemplate template = new VariableTemplate("alias.line1");
		WithTemplate withTemplate = new WithTemplate(withVar, "alias", template);
		Address address = new Address();
		address.setLine1("This line1 of address");
		Person person = new Person();
		person.setAddress(address);
		Scope scope = new Scope();
		scope.put("person", person);
		String result = withTemplate.apply(scope);
		assertEquals("Expecting line1 from address", "This line1 of address", result);
	}

}
