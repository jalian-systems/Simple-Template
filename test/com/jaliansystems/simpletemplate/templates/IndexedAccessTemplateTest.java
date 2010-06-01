package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;


public class IndexedAccessTemplateTest {

	@Test
	public void testAccessToStringCharacters() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(new LiteralTextTemplate("Hello"), new LiteralIntegerTemplate(2));
		String result = template.apply(new Scope());
		assertEquals("We are expecting a single character", "l", result);
	}

	@Test
	public void testAccessToAnElementOfAList() {
		IndexedAccessTemplate template = new IndexedAccessTemplate(
				new VariableTemplate("list"), new LiteralIntegerTemplate(2));
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
				new VariableTemplate("map"), new VariableTemplate("key"));
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
				new VariableTemplate("array"), new LiteralIntegerTemplate(1));
		Scope scope = new Scope();
		scope.put("array", new String[] { "One", "Two", "Three"});
		String result = template.apply(scope);
		assertEquals("Array item 2 should be second item", "Two", result);
	}

}
