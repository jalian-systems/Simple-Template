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
import com.jaliansystems.simpletemplate.internal.templates.LiteralIntegerTemplate;

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
