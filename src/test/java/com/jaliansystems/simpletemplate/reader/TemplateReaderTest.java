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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringReader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.Scope;
import com.jaliansystems.simpletemplate.Template;
import com.jaliansystems.simpletemplate.TemplateReader;
import com.jaliansystems.simpletemplate.internal.reader.LexerException;
import com.jaliansystems.simpletemplate.internal.reader.ParserException;
import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.Person;

public class TemplateReaderTest extends TemplateTestSuper {

	@Test
	public void testReadsLiteralText() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello World"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		assertEquals("Expect plain raw text", "Hello World",
				template.apply(new Scope()));
	}

	@Test
	public void testReadsLiteralWithEscapedDollors() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello \\$World"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		assertEquals("Expect escaped dollors", "Hello $World",
				template.apply(new Scope()));
	}

	@Test
	public void testReadsLiteralWithEscapedNewlines() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello \\nWorld\\\nNextLine"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		assertEquals("Expect escaped dollors", "Hello \nWorldNextLine",
				template.apply(new Scope()));
	}

	@Test
	public void testReadsVariableTemplates() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $name$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		scope.put("name", "GitHub");
		assertEquals("Expect escaped dollors", "Hello GitHub",
				template.apply(scope));
	}

	@Test
	public void testKeywordsDoNotNeedEscapesNotTrue() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $\\with$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		scope.put("with", "GitHub");
		assertEquals("Expect escaped dollors", "Hello GitHub",
				template.apply(scope));
	}

	@Test
	public void testPlainTemplateBlock() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"${ \"Hello \" $\\with$ }$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		scope.put("with", "GitHub");
		assertEquals("Expect escaped dollors", "Hello GitHub",
				template.apply(scope));
	}

	@Test
	public void testReadsVariableTemplatesWithQualifiedNames() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.name$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello GitHub",
				template.apply(scope));
	}

	@Test
	public void testReadsVariableTemplatesWithQualifiedNamesMultipleTimes()
			throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.name$. This is a $person.name$ greeting"),
				"<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors",
				"Hello GitHub. This is a GitHub greeting",
				template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplate() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.name[2]$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithTwoIndirections() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.names[1][2]$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setNames("GitHub", "Global Warming", "Iam OK");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello o", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithTwoIndirectionsWithSpaces()
			throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.names [ 1 ] [2]$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setNames("GitHub", "Global Warming", "Iam OK");
		scope.put("person", person);
		assertEquals("Expect escaped dollors", "Hello o", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithIdentifier() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.name[index]$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		scope.put("index", 2);
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testReadsIndexedTemplateWithIdentifierDoubleIndexing()
			throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $person.name[indices[2]]$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 });
		assertEquals("Expect escaped dollors", "Hello t", template.apply(scope));
	}

	@Test
	public void testIfTemplateWithSimpleExpressions() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $if person.minor { $person.name$ is a Kid }$"),
				"<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 });
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid ",
				template.apply(scope));
	}

	@Test
	public void testIfEscapeWorksForBlock() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $if person.minor\n { $person.name$ is a Kid \\}\\$ }$"),
				"<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		assertEquals("Expect escaped dollors", "Hello ", template.apply(scope));
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 });
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid }$ ",
				template.apply(scope));
	}

	@Test
	public void testWithTemplateWithSimpleExpressions() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $with person { $name$ is a Kid }$"), "<stream>", "$",
				"$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 });
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid ",
				template.apply(scope));
	}

	@Test
	public void testInsertingVariableIntoScope() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $set name to person.name $name$ is a Kid"), "<stream>",
				"$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		Person person = new Person();
		person.setName("GitHub");
		person.setMinor(true);
		scope.put("person", person);
		scope.put("indices", new Integer[] { 0, 1, 2, 3 });
		assertEquals("Expect escaped dollors", "Hello  GitHub is a Kid",
				template.apply(scope));
	}

	@Test
	public void testLoopingThroughAList() throws Exception {
		TemplateReader reader = new TemplateReader(new StringReader(
				"Hello $names { $it$, }$"), "<stream>", "$", "$");
		TemplateElement template = reader.readTemplate();
		assertTrue(template instanceof Template);
		Scope scope = new Scope();
		scope.put("names", new String[] { "Linus Torvalds", "Ken Thomson",
				"Dennis Richie" });
		assertEquals("Expect escaped dollors",
				"Hello  Linus Torvalds,  Ken Thomson,  Dennis Richie, ",
				template.apply(scope));
	}

	@Test
	public void testLiteralTextIsProcessed() throws Exception, LexerException,
			ParserException {
		templateAssert("Hello World", "Hello World", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testBackslashEscapesTemplateStart() throws Exception,
			LexerException, ParserException {
		templateAssert("Hello \\$World", "Hello $World", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testBackslashSubstitutesNewlines() throws Exception,
			LexerException, ParserException {
		templateAssert("Hello \\nWorld", "Hello \nWorld", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testVariableGetsSubstitutedByItsValue() throws Exception,
			LexerException, ParserException {
		Scope scope = new Scope();
		scope.put("world", "World");
		templateAssert("Hello $world$", "Hello World", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testABlockWithLiteralString() throws Exception, LexerException,
			ParserException {
		Scope scope = new Scope();
		scope.put("world", "World");
		templateAssert("${\"Hello \" $world$}$", "Hello World", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testABlockWithInteger() throws Exception, LexerException,
			ParserException {
		Scope scope = new Scope();
		scope.put("world", "World");
		templateAssert("${\"The population of india is \" 1139964932 }$",
				"The population of india is 1139964932", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testABlockWithBoolean() throws Exception, LexerException,
			ParserException {
		templateAssert("${\"Whether it is \" true \" or \" false }$",
				"Whether it is true or false", new Scope(),
				"The Input and Output should be same");
	}

	@Test(expected = ParserException.class)
	public void testAUnendedBlockThrowsAnException() throws Exception,
			LexerException, ParserException {
		Scope scope = new Scope();
		scope.put("world", "World");
		templateAssert("${\"Hello \" $world$", "Hello World", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testIndexedVariableGetsSubstitutedByItsValue() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { "World", "Universe" });
		templateAssert("Hello $greeting[1]$", "Hello Universe", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testLoopingOverAVariable() throws Exception, LexerException,
			ParserException {
		Scope scope = new Scope();
		scope.put("list", new String[] { "Bangalore", "Delhi", "Hyderabad" });
		templateAssert("$list $it$", "BangaloreDelhiHyderabad", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testLoopingOverAVariableWithABlock() throws Exception,
			LexerException, ParserException {
		Scope scope = new Scope();
		scope.put("list", new String[] { "Bangalore", "Delhi", "Hyderabad" });
		templateAssert("$list ${ $index1$ \": \" $it$ \"\\n\" }$",
				"1: Bangalore\n2: Delhi\n3: Hyderabad\n", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testIndexedVariableGetsSubstitutedByItsValueInMultiDimensionalArray()
			throws Exception {
		String[][][] u = new String[3][][];
		for (int i = 0; i < 3; i++) {
			String[][] v = new String[3][];
			for (int j = 0; j < 3; j++) {
				String[] w = new String[3];
				for (int k = 0; k < w.length; k++) {
					w[k] = "" + i + j + k;
				}
				v[j] = w;
			}
			u[i] = v;
		}
		Scope scope = new Scope();
		scope.put("u", u);
		templateAssert("$u[0][0][0]$", "000", scope,
				"The Input and Output should be same");
		templateAssert("$u[2][2][2]$", "222", scope,
				"The Input and Output should be same");
		templateAssert("$u[1][0][1]$", "101", scope,
				"The Input and Output should be same");
	}

	public static class MyString {
		private final String original;

		public MyString(String original) {
			this.original = original;
		}

		public int getLength() {
			return original.length();
		}

		public String[] getPresidents() {
			return new String[] { "Abdul Kalam", "Prathibha Patil" };
		}
	}

	@Test
	public void testIndexedVariablesMemberGetsSubstitutedByItsValue()
			throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new MyString[] { new MyString("World"),
				new MyString("Universe") });
		templateAssert("Hello $greeting[1].length$", "Hello 8", scope,
				"The Input and Output should be same");
		templateAssert("Hello $greeting[1].presidents[0]$",
				"Hello Abdul Kalam", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testIfTemplateExecutesTheTrueBranch() throws Exception,
			LexerException, ParserException {
		templateAssert("$if true \"Hello World\"", "Hello World", new Scope(),
				"The Input and Output should be same");

	}

	@Test
	public void testIfTemplateDoesnotExecuteTheFalseBranch() throws Exception {
		templateAssert("$if false \"Hello World\"", "", new Scope(),
				"The Input and Output should be same");

	}

	@Test
	public void testIfElseTemplateExecutesTheTrueBranch() throws Exception,
			LexerException, ParserException {
		templateAssert("$ifelse true \"Hello World\" \"Hello Universe\"",
				"Hello World", new Scope(),
				"The Input and Output should be same");

	}

	@Test
	public void testIfElseTemplateDoesnotExecuteTheFalseBranch()
			throws Exception {
		templateAssert("$ifelse false \"Hello World\" \"Hello Universe\"",
				"Hello Universe", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testWithAllowsShorthand() throws Exception, LexerException,
			ParserException {
		Scope scope = new Scope();
		scope.put("greeting", new MyString[] { new MyString("World"),
				new MyString("Universe") });
		templateAssert("Hello $with greeting[1] $length$", "Hello 8", scope,
				"The Input and Output should be same");
		templateAssert("Hello $with greeting[1] $presidents[0]$",
				"Hello Abdul Kalam", scope,
				"The Input and Output should be same");
		templateAssert("Hello $with greeting[1] {$presidents[0]$ }$",
				"Hello Abdul Kalam ", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testWithAllowsShorthandUsingAs() throws Exception,
			LexerException, ParserException {
		Scope scope = new Scope();
		scope.put("greeting", new MyString[] { new MyString("World"),
				new MyString("Universe") });
		templateAssert("Hello $with greeting[1] as p $p.length$", "Hello 8",
				scope, "The Input and Output should be same");
		templateAssert("Hello $with greeting[1] as q $q.presidents[0]$",
				"Hello Abdul Kalam", scope,
				"The Input and Output should be same");
		templateAssert("Hello $with greeting[1] as r {$r.presidents[0]$ }$",
				"Hello Abdul Kalam ", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testSetModifiesTheScope() throws Exception, LexerException,
			ParserException {
		Scope scope = new Scope();
		scope.put("greeting", new MyString[] { new MyString("World"),
				new MyString("Universe") });
		templateAssert("${$set p to greeting[1]}$Hello $p.length$", "Hello 8",
				scope, "The Input and Output should be same");
	}

	@Test
	public void testSubtemplateDefinition() throws Exception {
		templateAssert("$helloWorld() \"Hello World\\n\"", "", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testSubtemplateDefinitionWithParameters() throws Exception {
		templateAssert("$helloWorld(hello, world) ${ $hello$ \" \" $workd$ }$",
				"", new Scope(), "The Input and Output should be same");
	}

	@Test
	public void testSubtemplateCallWithParameters() throws Exception {
		templateAssert(
				"$helloWorld(hello, world) ${ $hello$ \" \" $world$ }$$:helloWorld(\"Hello\", \"World\")$",
				"Hello World", new Scope(),
				"The Input and Output should be same");
	}

	@Test
	public void testSubtemplateCallWithDefaultParameterAsAnIdentifier()
			throws Exception {
		Scope scope = new Scope();
		scope.put("hello", "Hello");
		templateAssert(
				"$helloWorld(hello, world) ${ $hello$ \" \" $world$ }$$hello:helloWorld(\"World\")$",
				"Hello World", scope, "The Input and Output should be same");
	}

	@Test
	public void testSubtemplateCallWithParametersAsIdentifiers()
			throws Exception {
		Scope scope = new Scope();
		scope.put("hello", "Hello");
		scope.put("world", "World");
		templateAssert(
				"$helloWorld(hello, world) ${ $hello$ \" \" $world$ }$$:helloWorld(hello, world)$",
				"Hello World", scope, "The Input and Output should be same");
	}

	@Test
	public void testSubtemplateCallChaining() throws Exception {
		Scope scope = new Scope();
		scope.put("hello", "Hello");
		templateAssert("$bold(item) ${\"<b>\" $item$ \"</b>\"}$"
				+ "$italics(item) ${\"<i>\" $item$ \"</i>\"}$"
				+ "$hello:bold():italics()$", "<i><b>Hello</b></i>", scope,
				"The Input and Output should be same");
	}

	@Test
	public void testSubtemplateCallWithAList() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { "Hello", "World" });
		templateAssert(
				"$helloWorld(list) ${ $list {$it$ }$}$$greeting:helloWorld()$",
				"Hello World ", scope, "The Input and Output should be same");
	}

	@Test
	public void testTemplateReaderAllowsSettingOfExpressionSeparators()
			throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { "Hello", "World" });
		templateAssert(
				"<<helloWorld(list) <<{ <<list {<<it>> }>>}>><<greeting:helloWorld()>>",
				"Hello World ", scope, "The Input and Output should be same",
				"<<", ">>");
	}

	@Test
	public void testTemplateReaderAcceptsURLAsConstructorParameter()
			throws Exception {
		File templateFile = new File(
				"test-data/testTemplateReaderAcceptsURLAsConstructorParameter.st");
		TemplateReader reader = new TemplateReader(
				templateFile.toURI().toURL(), "<<", ">>");
		TemplateElement template = reader.readTemplate();
		Scope scope = new Scope();
		scope.put("greeting", new String[] { "Hello", "World" });
		String result = template.apply(scope);
		assertEquals("The Input and Output should be same", "Hello World ",
				result);
	}

	@Test
	public void testKeywordsNeedNotBeQuotedInTemplateLexerContext()
			throws Exception, LexerException, ParserException {
		templateAssert("$if true \"Hello World\"", "Hello World", new Scope(),
				"The Input and Output should be same");
		templateAssert("${if true \"Hello World\"}$", "Hello World",
				new Scope(), "The Input and Output should be same");
	}

	@Test
	public void testTemplateReaderSupportsInclude() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", new String[] { "Hello", "World" });
		templateAssert(
				"<<include \"test-data/testTemplateReaderAcceptsURLAsConstructorParameter.st\"",
				"Hello World ", scope, "The Input and Output should be same",
				"<<", ">>");
	}

	@Test
	public void testMethodCallsKeepTypeInfoBetweenCalls() throws Exception {
		String methodDef = "$true2false(value) $ifelse value false true" ;
		String methodCall = "$ifelse value:true2false():true2false() true false" ;
		Scope scope = new Scope();
		scope.put("value", true);
		templateAssert(methodDef+methodCall, "true",
				scope, "The Input and Output should be same");
		scope.put("value", false);
		templateAssert(methodDef+methodCall, "false",
				scope, "The Input and Output should be same");
	}
	
	@Test
	public void testColonHandledProperly() throws Exception {
		Scope scope = new Scope();
		scope.put("greeting", "Hello");

		templateAssert("$greeting$: ", "Hello: ",
				scope, "The Input and Output should be same");
		templateAssert("$greeting$:", "Hello:",
				scope, "The Input and Output should be same");
	}
}
