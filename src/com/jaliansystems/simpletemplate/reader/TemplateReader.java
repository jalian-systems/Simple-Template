package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.ITemplateElement;
import com.jaliansystems.simpletemplate.templates.IfTemplate;
import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.LoopTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableScopeTemplate;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;
import com.jaliansystems.simpletemplate.templates.WithTemplate;

public class TemplateReader {

	private PushbackReader reader;
	private String lineno;
	private String position;

	private static class Identifier {
		public String text;
		public boolean isEscaped;
		public List<TemplateElement> indices = new ArrayList<TemplateElement>();
	}

	public TemplateReader(Reader reader) {
		this.reader = new PushbackReader(reader, 1024);
	}

	public ITemplateElement readTemplate(boolean isBlock) throws IOException {
		CompositeTemplate ct = new CompositeTemplate();
		int c;
		while ((c = reader.read()) != -1) {
			if (isBlock && c == '}') {
				int c1 = reader.read();
				if (c1 == '$' || c1 == -1)
					break;
				reader.unread(c1);
				isBlock = false;
			}
			if (c != '$') {
				reader.unread(c);
				LiteralTextTemplate ltt = readTextTemplate(isBlock);
				ct.add(ltt);
			} else {
				c = reader.read();
				if (c == '{') {
					ITemplateElement ct1 = readTemplate(true);
					ct.add(ct1);
				} else {
					reader.unread(c);
					Identifier identifier = readIdentifier();
					c = reader.read();
					if (c == -1)
						break;
					if (c == '$') {
						ct.add(createIdElement(identifier));
					} else {
						reader.unread(c);
						if (identifier.isEscaped) {
							ct.add(createLoopElement(identifier));
						} else if (identifier.text.equals("if")) {
							ct.add(createIfElement());
						} else if (identifier.text.equals("with")) {
							ct.add(createWithElement());
						} else if (identifier.text.equals("set")) {
							ct.add(createSetElement());
						} else {
							ct.add(createLoopElement(identifier));
						}
					}
				}
			}
		}
		return ct;
	}

	private ITemplateElement createLoopElement(Identifier identifier) throws IOException {
		skipSpaces();
		expect('{');
		ITemplateElement template = readTemplate(true);
		LoopTemplate lt = new LoopTemplate(createIdElement(identifier), template);
		return lt;
	}

	private ITemplateElement createSetElement() throws IOException {
		skipSpaces();
		String alias = readSimpleIdentifier();
		skipSpaces();
		expect("to");
		skipSpaces();
		Identifier identifier = readIdentifier();
		expect("$");
		VariableScopeTemplate vst = new VariableScopeTemplate(
				createIdElement(identifier), alias);
		return vst;
	}

	private void expect(String string) throws IOException {
		char[] cbuf = new char[string.length()];
		int read = reader.read(cbuf);
		String got = new String(cbuf);
		if (read < cbuf.length || !string.equals(got)) {
			throw new RuntimeException("Line No: " + lineno + " Position: "
					+ position + " Error: Expecting a 'to' got " + got);
		}
	}

	private String readSimpleIdentifier() throws IOException {
		int c = reader.read();
		if (c == -1 || !Character.isJavaIdentifierStart(c)) {
			throw new RuntimeException("Line No: " + lineno + " Position: "
					+ position + " Error: Expecting a simple identifier got "
					+ (c == -1 ? "EOF" : "'" + (char) c + "'"));
		}
		StringBuffer sb = new StringBuffer();
		sb.append((char) c);
		while ((c = reader.read()) != -1 && Character.isJavaIdentifierPart(c)) {
			sb.append((char) c);
		}
		if (c != -1)
			reader.unread(c);
		return sb.toString();
	}

	private ITemplateElement createWithElement() throws IOException {
		skipSpaces();
		Identifier identifier = readIdentifier();
		skipSpaces();
		expect('{');
		ITemplateElement block = readTemplate(true);
		WithTemplate wt = new WithTemplate(createIdElement(identifier), block);
		return wt;
	}

	private ITemplateElement createIfElement() throws IOException {
		skipSpaces();
		Identifier identifier = readIdentifier();
		skipSpaces();
		expect('{');
		ITemplateElement trueBranch = readTemplate(true);
		ITemplateElement falseBranch = null;
		skipSpaces();
		char[] ourElse = new char[4];
		int read = reader.read(ourElse);
		if (read < 4 || !"else".equals(new String(ourElse))) {
			for (int i = read - 1; i >= 0; i--)
				reader.unread(ourElse[i]);
		} else {
			skipSpaces();
			expect('{');
			falseBranch = readTemplate(true);
		}
		IfTemplate ift = new IfTemplate(createIdElement(identifier),
				trueBranch, falseBranch);
		return ift;
	}

	private TemplateElement createIdElement(Identifier identifier) {
		TemplateElement idElement;
		if (identifier.indices.size() == 0) {
			idElement = new VariableTemplate(identifier.text);
		} else {
			TemplateElement vt = new VariableTemplate(identifier.text);
			IndexedAccessTemplate iat = null;
			for (TemplateElement te : identifier.indices) {
				iat = new IndexedAccessTemplate(vt, te);
				vt = iat;
			}
			idElement = iat;
		}
		return idElement;
	}

	private Identifier readIdentifier() throws IOException {
		Identifier identifier = new Identifier();
		int c = reader.read();
		if (c == '\\') {
			identifier.isEscaped = true;
			c = reader.read();
		}
		StringBuffer sb = new StringBuffer();
		if (c == -1 || !Character.isJavaIdentifierStart((char) c)) {
			throw new RuntimeException("Line No: " + lineno + " Position: "
					+ position + " Error: Expecting a javaIdentifier got "
					+ (c == -1 ? "EOF" : "'" + (char) c + "'"));
		}
		sb.append((char) c);
		while ((c = reader.read()) != -1) {
			if ((c == '.' || Character.isJavaIdentifierPart((char) c)
					&& c != '$')) {
				sb.append((char) c);
			} else {
				reader.unread(c);
				break;
			}
		}
		identifier.text = sb.toString();
		collectIndices(identifier);
		return identifier;
	}

	private void collectIndices(Identifier identifier) throws IOException {
		skipSpaces();
		int c = reader.read();
		while (c == '[') {
			skipSpaces();
			c = reader.read();
			if (Character.isDigit((char) c)) {
				reader.unread(c);
				int index = readInteger();
				LiteralIntegerTemplate lit = new LiteralIntegerTemplate(index);
				identifier.indices.add(lit);
				skipSpaces();
				expect(']');
			} else if (Character.isJavaIdentifierStart(c)) {
				reader.unread(c);
				Identifier indexId = readIdentifier();
				identifier.indices.add(createIdElement(indexId));
				skipSpaces();
				expect(']');
			}
			skipSpaces();
			c = reader.read();
		}
		reader.unread(c);
	}

	private int readInteger() throws IOException {
		StringBuffer sb = new StringBuffer();
		int c = reader.read();
		while (c != -1 && Character.isDigit(c)) {
			sb.append((char) c);
			c = reader.read();
		}
		if (c != -1)
			reader.unread(c);
		return Integer.valueOf(sb.toString());
	}

	private void expect(char c) throws IOException {
		int ch = reader.read();
		if (c != ch)
			throw new RuntimeException("Line No: " + lineno + " Position: "
					+ position + " Error: Expecting a '" + c + "' Got "
					+ (ch == -1 ? "EOF" : "'" + (char) ch + "'"));
	}

	private void skipSpaces() throws IOException {
		int c = reader.read();
		while (c != -1 && Character.isWhitespace(c))
			c = reader.read();
		if (c != -1)
			reader.unread(c);
	}

	private LiteralTextTemplate readTextTemplate(boolean isBlock)
			throws IOException {
		int c;
		boolean escape = false;
		StringBuffer text = new StringBuffer();
		while ((c = reader.read()) != -1) {
			if (c == '\\') {
				escape = true;
			} else if (escape) {
				escape = false;
				if (c == '\n') {
				} else if (c == 'n') {
					text.append('\n');
				} else
					text.append((char) c);
			} else if (c == '$') {
				reader.unread(c);
				break;
			} else if (c == '}' && isBlock) {
				int c1 = reader.read();
				if (c1 == '$') {
					reader.unread(c1);
					reader.unread(c);
					break;
				} else {
					text.append((char) c);
					reader.unread(c1);
				}
			} else {
				text.append((char) c);
			}
		}
		return new LiteralTextTemplate(text.toString());
	}

	public ITemplateElement readTemplate() throws IOException {
		return readTemplate(false);
	}

}
