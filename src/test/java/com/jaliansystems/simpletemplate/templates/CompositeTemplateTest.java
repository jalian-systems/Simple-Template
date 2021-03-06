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
import com.jaliansystems.simpletemplate.internal.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.internal.templates.LiteralTextTemplate;


public class CompositeTemplateTest {

	@Test
	public void testCompositeTemplate() {
		Template ct = new Template(null, 0);
		ct.add(new LiteralTextTemplate("This is a composite template with ", null, 0));
		ct.add(new LiteralIntegerTemplate(3, null, 0));
		ct.add(new LiteralTextTemplate(" elements", null, 0));
		String result = ct.apply(new Scope());
		assertEquals("Composite element just concatenates its elements", "This is a composite template with 3 elements", result);
	}
}
