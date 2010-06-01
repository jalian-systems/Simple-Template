package com.jaliansystems.simpletemplate.templates;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.Log;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;


public class VariableTemplateTest {
	
	private ByteArrayOutputStream baos;

	@Before
	public void setup() {
		baos = new ByteArrayOutputStream();
		Log.setWriter(new PrintStream(baos));
	}
	
	@After
	public void teardown() {
		Log.reset();
	}
	
	@Test
	public void testWithSingleProperty() {
		VariableTemplate svt = new VariableTemplate("greeting");
		Scope scope = new Scope();
		scope.put("greeting", "Hello World");
		String result = svt.apply(scope);
		assertEquals("Hello World", result);
	}
	
	@Test
	public void testWithAccessThroughObject() {
		VariableTemplate svt = new VariableTemplate("helloWorld.greeting");
		Scope scope = new Scope();
		scope.put("helloWorld", new Object() {
			@SuppressWarnings("unused")
			public String getGreeting() {
				return "Hello World" ;
			}
		});
		String result = svt.apply(scope);
		assertEquals("Hello World", result);
	}

	@Test
	public void testWithAccessThroughObjectWithIs() {
		VariableTemplate svt = new VariableTemplate("person.minor");
		Scope scope = new Scope();
		scope.put("person", new Object() {
			@SuppressWarnings("unused")
			public boolean isMinor() {
				return true ;
			}
		});
		String result = svt.apply(scope);
		assertEquals("true", result);
	}

	@Test
	public void testUnavailableAttrReturnsNone() {
		VariableTemplate svt = new VariableTemplate("person.minor");
		Scope scope = new Scope();
		scope.put("person", new Object());
		String result = svt.apply(scope);
		assertEquals("", result);
	}

	@Test
	public void testAsBinary() {
		testAsBinary("true", "A true string is true", true);
		testAsBinary("", "An empty string is still true", true);
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
		VariableTemplate svt = new VariableTemplate("foo");
		Scope scope = new Scope();
		scope.put("foo", o);
		assertEquals(message, expected, svt.asBinary(scope));
	}
}
