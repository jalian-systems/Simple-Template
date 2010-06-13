package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ListToStringTest {

	@Test
	public void testDefaultList() {
		VariableTemplate vt = new VariableTemplate("list.toString", "<string>", 1);
		Scope scope = new Scope();
		ArrayList<String> value = new ArrayList<String>();
		value.add("One");
		value.add("Two");
		value.add("Three");
		scope.put("list", value);
		String result = vt.apply(scope);
		assertEquals("[One, Two, Three]", result);
	}

	@Test
	public void testDefaultListSeparators() {
		VariableTemplate vt = new VariableTemplate("list", "<string>", 1);
		Scope scope = new Scope();
		scope.put("list", new String[] { "One", "Two", "Three" });
		String result = vt.apply(scope);
		assertEquals("One,Two,Three", result);
	}

	@Test
	public void testCustomListSeparators() {
		VariableTemplate vt = new VariableTemplate("list", "<string>", 1);
		Scope scope = new Scope();
		scope.put("list", new String[] { "One", "Two", "Three" });
		scope.put("st_list_separator", "-");
		String result = vt.apply(scope);
		assertEquals("One-Two-Three", result);
	}

	@Test
	public void testCustomListSeparatorsWithPrefixAndSuffix() {
		VariableTemplate vt = new VariableTemplate("list", "<string>", 1);
		Scope scope = new Scope();
		scope.put("list", new String[] { "One", "Two", "Three" });
		scope.put("st_list_separator", "-");
		scope.put("st_list_prefix", "<");
		scope.put("st_list_suffix", ">");
		String result = vt.apply(scope);
		assertEquals("<One>-<Two>-<Three>", result);
	}

}
