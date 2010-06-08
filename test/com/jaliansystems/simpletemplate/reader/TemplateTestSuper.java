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

import static org.junit.Assert.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class TemplateTestSuper {

	public TemplateTestSuper() {
		super();
	}

	protected void templateAssert(String templateText, String expected, Scope scope,
			String message) throws Exception {
		templateAssert(templateText, expected, scope, message, null, null);
			}

	protected void templateAssert(String templateText, String expected, Scope scope,
			String message, String start, String end) throws Exception {
				Reader in = new StringReader(templateText);
				ITemplateReader reader = new TemplateReader(in, "<stream>", start, end);
				TemplateElement template = reader.readTemplate();
				String result = template.apply(scope);
				assertEquals(message, expected, result);
			}

}