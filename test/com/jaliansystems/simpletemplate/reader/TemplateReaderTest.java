package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.ITemplateElement;
import com.jaliansystems.simpletemplate.templates.Person;
import com.jaliansystems.simpletemplate.templates.Scope;


public class TemplateReaderTest {

	@Test
	public void testReadsLiteralText() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello World"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		assertEquals("Expect plain raw text", "Hello World", template.apply(new Scope()));
	}

	@Test
	public void testReadsLiteralWithEscapedDollors() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello \\$World"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		assertEquals("Expect escaped dollors", "Hello $World", template.apply(new Scope()));
	}

	@Test
	public void testReadsLiteralWithEscapedNewlines() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello \\nWorld\\\nNextLine"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		assertEquals("Expect escaped dollors", "Hello \nWorldNextLine", template.apply(new Scope()));
	}

	@Test
	public void testReadsVariableTemplates() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $name$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		scope.put("name", "GitHub");
		assertEquals("Expect escaped dollors", "Hello GitHub", template.apply(scope));
	}

	@Test
	public void testKeywordsDoNotNeedEscapes() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $with$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		scope.put("with", "GitHub");
		assertEquals("Expect escaped dollors", "Hello GitHub", template.apply(scope));
	}
	
	@Test
	public void testPlainTemplateBlock() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("${ Hello $with$ }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", " Hello  ", template.apply(scope));
		scope.put("with", "GitHub");
		assertEquals("Expect escaped dollors", " Hello GitHub ", template.apply(scope));
	}

	@Test
	public void testReadsVariableTemplatesWithQualifiedNames() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.name$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello GitHub", template.apply(scope));
	}

	@Test
	public void testReadsVariableTemplatesWithQualifiedNamesMultipleTimes() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.name$. This is a $person.name$ greeting"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello GitHub. This is a GitHub greeting", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplate() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.name[2]$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithTwoIndirections() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.names[1][2]$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setNames("GitHub", "Global Warming", "Iam OK");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello o", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithTwoIndirectionsWithSpaces() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.names [ 1 ] [2]$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setNames("GitHub", "Global Warming", "Iam OK");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello o", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithIdentifier() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.name[index]$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		scope.put("index", 2);
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithIdentifierDoubleIndexing() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $person.name[indices[2]]$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testIfTemplateWithSimpleExpressions() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $if person.minor { $person.name$ is a Kid }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid ", template.apply(scope));
	}

	@Test
	public void testIfEscapeWorksForBlock() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $if person.minor\n { $person.name$ is a Kid \\}\\$ }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid }$ ", template.apply(scope));
	}

	@Test
	public void testWithTemplateWithSimpleExpressions() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $with person { $name$ is a Kid }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid ", template.apply(scope));
	}

	@Test
	public void testIfElseTemplateWithSimpleExpressions() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $if person.minor { $person.name$ is a Kid }$ else { $person.name$ is a major }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid ", template.apply(scope));
	}

	@Test
	public void testInsertingVariableIntoScope() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $set name to person.name$ $name$ is a Kid"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 } );
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid", template.apply(scope));
	}

	@Test
	public void testLoopingThroughAList() throws IOException {
		TemplateReader reader = new TemplateReader(new StringReader("Hello $names { $it$, }$"));
		ITemplateElement template = reader.readTemplate();
		assertTrue(template instanceof CompositeTemplate);
		Scope scope = new Scope();
		scope.put("names", new String[] { "Linus Torvalds", "Ken Thomson", "Dennis Richie" });
		assertEquals("Expect escaped dollors", "Hello  Linus Torvalds,  Ken Thomson,  Dennis Richie, ", template.apply(scope));
	}

}
