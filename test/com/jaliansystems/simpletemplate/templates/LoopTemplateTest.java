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
import java.util.Map;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.LoopTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;


public class LoopTemplateTest {

	@Test
	public void testListItemsAreUsed() {
		List<String> items = new ArrayList<String>();
		items.add("Bangalore");
		items.add("Hyderabad");
		items.add("Delhi");
		
		VariableTemplate vtIndex0 = new VariableTemplate("index0", null, 0);
		VariableTemplate vtIndex1 = new VariableTemplate("index1", null, 0);
		VariableTemplate vtItem = new VariableTemplate("it", null, 0);
		
		CompositeTemplate ct = new CompositeTemplate(null, 0);
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtItem);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		
		VariableTemplate cities = new VariableTemplate("cities", null, 0);
		
		Scope scope = new Scope();
		scope.put("cities", items);
		
		LoopTemplate lt = new LoopTemplate(cities, ct, null, 0);
		String result = lt.apply(scope);
		
		assertEquals("Loop should produce concatenated list of items", "0 1 Bangalore 1 2 Hyderabad 2 3 Delhi ", result);
	}

	@Test
	public void testArrayItemsAreUsed() {
		VariableTemplate vtIndex0 = new VariableTemplate("index0", null, 0);
		VariableTemplate vtIndex1 = new VariableTemplate("index1", null, 0);
		VariableTemplate vtItem = new VariableTemplate("it", null, 0);
		
		CompositeTemplate ct = new CompositeTemplate(null, 0);
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtItem);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		
		VariableTemplate cities = new VariableTemplate("cities", null, 0);
		
		Scope scope = new Scope();
		scope.put("cities", new String[] {"Bangalore", "Hyderabad", "Delhi"});
		
		LoopTemplate lt = new LoopTemplate(cities, ct, null, 0);
		String result = lt.apply(scope);
		
		assertEquals("Loop should produce concatenated list of items", "0 1 Bangalore 1 2 Hyderabad 2 3 Delhi ", result);
	}

	@Test
	public void testMapItemsAreUsed() {
		Map<String, String> items = new HashMap<String, String>();
		items.put("India", "Delhi");
		items.put("US", "Washington");
		items.put("Srilanka", "Colombo");
		
		VariableTemplate vtIndex0 = new VariableTemplate("index0", null, 0);
		VariableTemplate vtIndex1 = new VariableTemplate("index1", null, 0);
		VariableTemplate vtKey = new VariableTemplate("key", null, 0);
		VariableTemplate vtValue = new VariableTemplate("value", null, 0);
		
		CompositeTemplate ct = new CompositeTemplate(null, 0);
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtKey);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		ct.add(vtValue);
		ct.add(new LiteralTextTemplate(" ", null, 0));
		
		VariableTemplate cities = new VariableTemplate("cities", null, 0);
		
		Scope scope = new Scope();
		scope.put("cities", items);
		
		LoopTemplate lt = new LoopTemplate(cities, ct, null, 0);
		String result = lt.apply(scope);
		
		assertTrue("Loop should produce concatenated list of items", result.contains("India Delhi "));
		assertTrue("Loop should produce concatenated list of items", result.contains("US Washington "));
		assertTrue("Loop should produce concatenated list of items", result.contains("Srilanka Colombo "));
	}
}
