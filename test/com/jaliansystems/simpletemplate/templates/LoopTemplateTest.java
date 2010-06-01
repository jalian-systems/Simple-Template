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
		
		VariableTemplate vtIndex0 = new VariableTemplate("index0");
		VariableTemplate vtIndex1 = new VariableTemplate("index1");
		VariableTemplate vtItem = new VariableTemplate("it");
		
		CompositeTemplate ct = new CompositeTemplate();
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtItem);
		ct.add(new LiteralTextTemplate(" "));
		
		VariableTemplate cities = new VariableTemplate("cities");
		
		Scope scope = new Scope();
		scope.put("cities", items);
		
		LoopTemplate lt = new LoopTemplate(cities, ct);
		String result = lt.apply(scope);
		
		assertEquals("Loop should produce concatenated list of items", "0 1 Bangalore 1 2 Hyderabad 2 3 Delhi ", result);
	}

	@Test
	public void testArrayItemsAreUsed() {
		VariableTemplate vtIndex0 = new VariableTemplate("index0");
		VariableTemplate vtIndex1 = new VariableTemplate("index1");
		VariableTemplate vtItem = new VariableTemplate("it");
		
		CompositeTemplate ct = new CompositeTemplate();
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtItem);
		ct.add(new LiteralTextTemplate(" "));
		
		VariableTemplate cities = new VariableTemplate("cities");
		
		Scope scope = new Scope();
		scope.put("cities", new String[] {"Bangalore", "Hyderabad", "Delhi"});
		
		LoopTemplate lt = new LoopTemplate(cities, ct);
		String result = lt.apply(scope);
		
		assertEquals("Loop should produce concatenated list of items", "0 1 Bangalore 1 2 Hyderabad 2 3 Delhi ", result);
	}

	@Test
	public void testMapItemsAreUsed() {
		Map<String, String> items = new HashMap<String, String>();
		items.put("India", "Delhi");
		items.put("US", "Washington");
		items.put("Srilanka", "Colombo");
		
		VariableTemplate vtIndex0 = new VariableTemplate("index0");
		VariableTemplate vtIndex1 = new VariableTemplate("index1");
		VariableTemplate vtKey = new VariableTemplate("key");
		VariableTemplate vtValue = new VariableTemplate("value");
		
		CompositeTemplate ct = new CompositeTemplate();
		ct.add(vtIndex0);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtIndex1);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtKey);
		ct.add(new LiteralTextTemplate(" "));
		ct.add(vtValue);
		ct.add(new LiteralTextTemplate(" "));
		
		VariableTemplate cities = new VariableTemplate("cities");
		
		Scope scope = new Scope();
		scope.put("cities", items);
		
		LoopTemplate lt = new LoopTemplate(cities, ct);
		String result = lt.apply(scope);
		
		assertTrue("Loop should produce concatenated list of items", result.contains("India Delhi "));
		assertTrue("Loop should produce concatenated list of items", result.contains("US Washington "));
		assertTrue("Loop should produce concatenated list of items", result.contains("Srilanka Colombo "));
	}
}
