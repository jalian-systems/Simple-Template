package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.Reader;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.Log;
import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class TemplateReader implements ITemplateReader {

	private final LexerReader in;
	private final ILexer textLexer;
	private final ILexer templateLexer;

	public TemplateReader(Reader in, String fileName) {
		this.in = new LexerReader(in, fileName);
		LexerMaintainer maintainer = new LexerMaintainer();
		textLexer = new TextLexer(this.in, maintainer);
		templateLexer = new TemplateLexer(this.in, maintainer);
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
		CompositeTemplate ct = new CompositeTemplate(in.getFileName(),
				in.getLineNumber());
		Token t;
		while ((t = textLexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_EOF)).getType() != TokenType.TT_EOF) {
			ct.add(t.extract());
		}
		return ct;
	}
}
