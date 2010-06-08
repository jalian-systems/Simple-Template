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

import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;


public class LiteralTextTemplateTest {

	@Test
	public void textTemplateDoesNotDoSubstitutions() {
		String text = "A tempting $way$ for failing $test$ case";
		LiteralTextTemplate template = new LiteralTextTemplate(text, text, 0);
		Scope scope = new Scope();
		scope.put("way", "This is my way");
		scope.put("test", "Of testing");
		String result = template.apply(scope);
		assertEquals("Expect literally what we passed", text, result);
	}

	@Test
	public void testAsBinary() {
		assertEquals("A true text is always true", true, new LiteralTextTemplate("true", null, 0).asBinary(new Scope()));
		assertEquals("Anyother text is false", true, new LiteralTextTemplate("", null, 0).asBinary(new Scope()));
		assertEquals("Anyother text is false", true, new LiteralTextTemplate("some text with true in it", null, 0).asBinary(new Scope()));
	}
}
