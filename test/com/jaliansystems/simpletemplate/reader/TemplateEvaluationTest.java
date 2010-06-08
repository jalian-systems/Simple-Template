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

package com.jaliansystems.simpletemplate.reader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.Scope;


public class TemplateEvaluationTest extends TemplateTestSuper {
	
	@Test
	public void testEvaluatesMethodsWithoutPrevixes() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { new String("World"),
				new String("Universe") });
		templateAssert("Hello $greeting[1].length$", "Hello 8", scope,
				"The Input and Output should be same");
	}

}
