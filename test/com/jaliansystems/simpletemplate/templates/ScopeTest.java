package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.Scope;


public class ScopeTest {

	@Test
	public void testChecksInItsScopeBeforeParents() {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent);
		child.put("Key", "Childs Value");
		assertEquals("Parents key value overridden by child", "Childs Value", child.resolve("Key"));
	}
	
	@Test
	public void testReturnsValueFromParentIfNotExistInChild() {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent);
		child.put("Key1", "Childs Value");
		assertEquals("Parents key value given if child doesnt have one", "Parents Value", child.resolve("Key"));
	}
	
	@Test
	public void testScopeWithAObjectChecksTheObjectBeforeTheParent() {
		Scope parent = new Scope();
		parent.put("Key", "Parents Value");
		Scope child = new Scope(parent, new Object() {
			@SuppressWarnings("unused")
			public String getKey() {
				return "Objects Value" ;
			}
		});
		assertEquals("Objects value is checked before the parents", "Objects Value", child.resolve("key"));
	}
}
