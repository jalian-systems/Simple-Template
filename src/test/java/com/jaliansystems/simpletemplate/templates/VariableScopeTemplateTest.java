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
import com.jaliansystems.simpletemplate.Template;
import com.jaliansystems.simpletemplate.internal.templates.VariableScopeTemplate;
import com.jaliansystems.simpletemplate.internal.templates.VariableTemplate;


public class VariableScopeTemplateTest {

	@Test
	public void testIntroducingAVariableGivesAShortHand() {
		VariableTemplate withVar = new VariableTemplate("person.address", null, 0);
		VariableTemplate template = new VariableTemplate("alias.line1", null, 0);
		VariableScopeTemplate vst = new VariableScopeTemplate(withVar, "alias", null, 0);
		
		Template ct = new Template(null, 0);
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
