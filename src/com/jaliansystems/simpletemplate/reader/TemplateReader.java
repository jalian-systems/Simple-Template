package com.jaliansystems.simpletemplate.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.Log;
import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class TemplateReader implements ITemplateReader {

	private final LexerReader lexerReader;
	private final ILexer textLexer;
	private final ILexer templateLexer;

	public TemplateReader(Reader in, String fileName, String startToken, String endToken) throws Exception {
		this(new LexerReader(new File(".").toURI().toURL(), in, fileName, startToken, endToken));
	}

	public TemplateReader(URL url, String startToken, String endToken) throws Exception {
		this(new LexerReader(url, new InputStreamReader(url.openStream()), url.getFile(), startToken, endToken));
	}

	private TemplateReader(LexerReader in) {
		this.lexerReader = in;
		LexerMaintainer maintainer = new LexerMaintainer();
		textLexer = new TextLexer(this.lexerReader, maintainer);
		templateLexer = new TemplateLexer(this.lexerReader, maintainer);
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
		while ((t = textLexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_EOF)).getType() != TokenType.TT_EOF) {
			ct.add(t.extract());
		}
		return ct;
	}
}
