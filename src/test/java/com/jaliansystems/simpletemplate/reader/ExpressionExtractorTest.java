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

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.internal.reader.ExpressionExtractor;
import com.jaliansystems.simpletemplate.internal.reader.LexerReader;
import com.jaliansystems.simpletemplate.internal.reader.TemplateLexer;
import com.jaliansystems.simpletemplate.internal.reader.Token;
import com.jaliansystems.simpletemplate.internal.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.internal.templates.MethodCallTemplate;
import com.jaliansystems.simpletemplate.internal.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.internal.templates.TemplateElement;
import com.jaliansystems.simpletemplate.internal.templates.VariableTemplate;


public class ExpressionExtractorTest {

	@Test
	public void testSimpleName() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertTrue(template instanceof VariableTemplate);
	}

	@Test
	public void testQualifiedName() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertTrue(template instanceof VariableTemplate);
	}

	@Test
	public void testIndexedAccess() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list[10]"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(IndexedAccessTemplate.class.getName(), template.getClass().getName());
	}

	@Test
	public void testMethodCall() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list:f()"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(MethodCallTemplate.class.getName(), template.getClass().getName());
	}
	
	@Test
	public void testMethodCallChain() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list:f():g()"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(MethodCallTemplate.class.getName(), template.getClass().getName());
	}
	
	@Test
	public void testIndexedAccessChain() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list[10][1]"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(IndexedAccessTemplate.class.getName(), template.getClass().getName());
	}

	@Test
	public void testIndexedAccessAttributeAccess() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list[10].m1.m2"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(ObjectScopeTemplate.class.getName(), template.getClass().getName());
	}

	@Test
	public void testIndexedAccessMethodCall() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list[10]:f()"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(MethodCallTemplate.class.getName(), template.getClass().getName());
	}

	@Test
	public void testMethodCallIndexedAccess() throws Exception {
		TemplateLexer lexer = new TemplateLexer(new LexerReader(null, new StringReader("value.list[10]:f()[20]"), "<stream>", "$", "$"));
		Token token = lexer.nextToken();
		TemplateElement template = new ExpressionExtractor().extract(token, lexer);
		assertEquals(IndexedAccessTemplate.class.getName(), template.getClass().getName());
	}
}
