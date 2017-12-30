/*
 *   Copyright 2010 Jalian Systems Pvt. Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/

package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jaliansystems.simpletemplate.Scope;
import com.jaliansystems.simpletemplate.internal.templates.VariableTemplate;
import com.jaliansystems.simpletemplate.internal.templates.WithTemplate;


public class WithTemplateTest {

	@Test
	public void testGetsResolvesInLocalScope() {
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("line1", null, 0);
		WithTemplate withTemplate = new WithTemplate(withVar, template, null, 0);
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
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("line1", null, 0);
		WithTemplate withTemplate = new WithTemplate(withVar, template, null, 0);
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
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("_line1", null, 0);
		WithTemplate withTemplate = new WithTemplate(withVar, template, null, 0);
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
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("alias.line1", null, 0);
		WithTemplate withTemplate = new WithTemplate(withVar, "alias", template, null, 0);
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
