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
import com.jaliansystems.simpletemplate.internal.templates.IfTemplate;
import com.jaliansystems.simpletemplate.internal.templates.LiteralBooleanTemplate;
import com.jaliansystems.simpletemplate.internal.templates.LiteralTextTemplate;

public class IfTemplateTest {

	@Test
	public void testIfTemplateWithSimpleCondition() {
		LiteralTextTemplate trueBranch = new LiteralTextTemplate("This is true", null, 0);
		LiteralTextTemplate falseBranch = new LiteralTextTemplate(
				"This is false", null, 0);

		assertEquals("IfTemplate should return the true one", "This is true",
				new IfTemplate(new LiteralBooleanTemplate(true, null, 0), trueBranch,
						falseBranch, null, 0).apply(new Scope()));
		assertEquals("IfTemplate should return the false one", "This is false",
				new IfTemplate(new LiteralBooleanTemplate(false, null, 0), trueBranch,
						falseBranch, null, 0).apply(new Scope()));
	}
}
