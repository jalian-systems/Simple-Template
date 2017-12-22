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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.Log;
import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class TemplateReader implements ITemplateReader {

	private static final String DEFAULT_START_TOKEN = "$";
	private static final String DEFAULT_END_TOKEN = "$";

	private final LexerReader lexerReader;
	private LexerMaintainer lexerMaintainer;
	private TextLexer textLexer;
	private TemplateLexer templateLexer;

	public TemplateReader(Reader in, String fileName, String startToken,
			String endToken) throws Exception {
		this(new LexerReader(new File(".").toURI().toURL(), in, fileName,
				startToken, endToken));
	}

	public TemplateReader(URL url, String startToken, String endToken)
			throws Exception {
		this(new LexerReader(url, new InputStreamReader(url.openStream()),
				url.getFile(), startToken, endToken));
	}

	public static TemplateReader fromString(String s) throws Exception {
		return fromString(s, DEFAULT_START_TOKEN, DEFAULT_END_TOKEN);
	}

	public static TemplateReader fromString(String s, String startToken,
			String endToken) throws Exception {
		return new TemplateReader(new StringReader(s), "<string>", startToken,
				endToken);
	}

	public static TemplateReader fromFile(String fileName, String startToken,
			String endToken) throws Exception {
		return new TemplateReader(new File(fileName).toURI().toURL(),
				startToken, endToken);
	}

	public static TemplateReader fromFile(String fileName) throws Exception {
		return fromFile(fileName, DEFAULT_START_TOKEN, DEFAULT_END_TOKEN);
	}

	public static TemplateReader fromResource(String resource,
			String startToken, String endToken) throws Exception {
		return new TemplateReader(TemplateReader.class.getClassLoader()
				.getResource(resource), startToken, endToken);
	}

	public static TemplateReader fromResource(String resource) throws Exception {
		return fromResource(resource, DEFAULT_START_TOKEN, DEFAULT_END_TOKEN);
	}

	private TemplateReader(LexerReader lexerReader) {
		this.lexerReader = lexerReader;
		lexerMaintainer = new LexerMaintainer();
		textLexer = new TextLexer(this.lexerReader, lexerMaintainer);
		templateLexer = new TemplateLexer(this.lexerReader, lexerMaintainer);
		setLexersForTokenTypes();
	}

	private void setLexersForTokenTypes() {
		TokenType[] values = TokenType.values();
		for (TokenType tokenType : values) {
			if (tokenType.isExtractable()) {
				tokenType.setLexer(templateLexer);
			}
			if (tokenType == TokenType.TT_CBLOCK_START)
				tokenType.setLexer(textLexer);
		}
	}

	public void setEvaluationMode(EvaluationMode mode) {
		Log.setMode(mode);
	}

	@Override
	public TemplateElement readTemplate() throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate(lexerReader.getFileName(),
				lexerReader.getLineNumber());
		Token t;
		while ((t = textLexer.expect1(TokenType.getExtractableTokens(),
				TokenType.TT_EOF)).getType() != TokenType.TT_EOF) {
			ct.add(t.extract());
			if (t.getType() == TokenType.TT_INCLUDE)
				setLexersForTokenTypes();
		}
		lexerMaintainer.remove(textLexer);
		lexerMaintainer.remove(templateLexer);
		return ct;
	}
}
