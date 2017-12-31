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

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.Scope;
import com.jaliansystems.simpletemplate.internal.reader.Log;
import com.jaliansystems.simpletemplate.internal.templates.EvaluationError;
import com.jaliansystems.simpletemplate.internal.templates.VariableTemplate;

public class VariableTemplateTest {
	
	public static final class ObjectExtension2 {
		public boolean isMinor() {
			return true ;
		}
	}

	public static final class ObjectExtension {
		public String getGreeting() {
			return "Hello World" ;
		}
	}

	@After
	public void resetLog() {
		Log.setMode(EvaluationMode.RELAXED);
	}
	
	@Test
	public void testWithSingleProperty() {
		VariableTemplate svt = new VariableTemplate("greeting", null, 0);
		Scope scope = new Scope();
		scope.put("greeting", "Hello World");
		String result = svt.apply(scope);
		assertEquals("Hello World", result);
	}
	
	@Test
	public void testWithAccessThroughObject() {
		VariableTemplate svt = new VariableTemplate("helloWorld.greeting", null, 0);
		Scope scope = new Scope();
		scope.put("helloWorld", new ObjectExtension());
		String result = svt.apply(scope);
		assertEquals("Hello World", result);
	}

	@Test
	public void testWithAccessThroughObjectWithIs() {
		VariableTemplate svt = new VariableTemplate("person.minor", null, 0);
		Scope scope = new Scope();
		scope.put("person", new ObjectExtension2());
		String result = svt.apply(scope);
		assertEquals("true", result);
	}

	@Test
	public void testUnavailableAttrReturnsNone() {
		VariableTemplate svt = new VariableTemplate("person.minor", "<stream>", 0);
		Scope scope = new Scope();
		scope.put("person", new Object());
		String result = svt.apply(scope);
		assertEquals("", result);
	}

	@Test(expected=EvaluationError.class)
	public void testUnavailableAttrThrowsExceptionInStrictMode() {
		Log.setMode(EvaluationMode.STRICT);
		VariableTemplate svt = new VariableTemplate("person.minor", "<stream>", 0);
		Scope scope = new Scope();
		scope.put("person", new Object());
		String result = svt.apply(scope);
		assertEquals("", result);
	}

	@Test
	public void testAsBinary() {
		testAsBinary("true", "A true string is true", true);
		testAsBinary("", "An empty string is false", false);
		testAsBinary("anyother", "Any string other than true is false", true);
		testAsBinary("TrUe", "You can't hide truth by case changing", true);
		testAsBinary(null, "A null object is false", false);
		testAsBinary(new Object(), "A non null object is a true (provided it is not a string)", true);
		testAsBinary(new ArrayList<String>(), "An empty list is false", false);
		ArrayList<String> o = new ArrayList<String>();
		o.add("Make me true");
		testAsBinary(o, "A non empty list is true", true);
		testAsBinary(new HashMap<String, String>(), "An empty map is also false", false);
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("Make me", "truthful");
		testAsBinary(h, "A non empty map is obviously true", true);
	}

	private void testAsBinary(Object o, String message, boolean expected) {
		VariableTemplate svt = new VariableTemplate("foo", null, 0);
		Scope scope = new Scope();
		scope.put("foo", o);
		assertEquals(message, expected, svt.asBinary(scope));
	}
}
