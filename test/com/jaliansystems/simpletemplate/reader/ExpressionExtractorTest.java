package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.MethodCallTemplate;
import com.jaliansystems.simpletemplate.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;


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
