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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.jaliansystems.simpletemplate.Scope;
import com.jaliansystems.simpletemplate.internal.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.internal.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.internal.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.internal.templates.VariableTemplate;


public class IndexedAccessTemplateTest {

	@Test
	public void testAccessToStringCharacters() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new LiteralTextTemplate("Hello", null, 0),
				new LiteralIntegerTemplate(2, null, 0), null, 0);
		String result = template.apply(new Scope());
		assertEquals("We are expecting a single character", "l", result);
	}

	@Test
	public void testAccessToAnElementOfAList() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new VariableTemplate("list", null, 0), new LiteralIntegerTemplate(2, null, 0), null, 0);
		List<String> list = new ArrayList<String>();
		list.add("Hello");
		list.add("World");
		list.add("This is the second item");
		list.add("This is the third item");
		Scope scope = new Scope();
		scope.put("list", list);
		String result = template.apply(scope);
		assertEquals("List item 2 should be second item", "This is the second item", result);
	}

	@Test
	public void testAccessToAnElementOfAMap() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new VariableTemplate("map", null, 0), new VariableTemplate("key", null, 0), null, 0);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("First", "1st");
		map.put("Second", "2nd");
		map.put("Third", "3rd");
		Scope scope = new Scope();
		scope.put("map", map);
		scope.put("key", "Second");
		String result = template.apply(scope);
		assertEquals("Map item 2 should be second item", "2nd", result);
	}

	@Test
	public void testAccessToAnElementOfAnArray() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new VariableTemplate("array", null, 0), new LiteralIntegerTemplate(1, null, 0), null, 0);
		Scope scope = new Scope();
		scope.put("array", new String[] { "One", "Two", "Three"});
		String result = template.apply(scope);
		assertEquals("Array item 2 should be second item", "Two", result);
	}

	@Test
	public void testAccessUsingInvalidIndex() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new VariableTemplate("array", null, 0), new VariableTemplate("index", null, 0), null, 0);
		Scope scope = new Scope();
		scope.put("array", new String[] { "One", "Two", "Three"});
		String result = template.apply(scope);
		assertEquals("Array item 2 should be second item", "", result);
	}
}
