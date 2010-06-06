package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public enum TokenType {
	TT_END_TEMPLATE,
	TT_WITH,
	TT_SET,
	TT_IF,
	TT_IFELSE,
	TT_CBLOCK_START,
	TT_BLOCK_START,
	TT_BLOCK_END,
	TT_OPEN_BR,
	TT_CLOSE_BR,
	TT_NAME_SEPARATOR,
	TT_TO,
	TT_AS,
	TT_TRUE,
	TT_FALSE,
	TT_START_IDENTIFIER,
	TT_IDENTIFIER,
	TT_INTEGER,
	TT_STRING,
	TT_BOOLEAN,
	TT_TEXT,
	TT_EOF,
	TT_ALIAS;
	
	private IExtractTemplate extractTemplate ;
	private String readableString ;
	
	TokenType() {
		setUpReadableString();
	}
	
	private void setUpReadableString() {
		String s = super.toString().toLowerCase();
		char[] charArray = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean camel = true ;
		for (int i = 3; i < charArray.length; i++) {
			char c = charArray[i];
			if (c == '_') {
				camel = true ;
				continue ;
			}
			else if (camel)
				c = Character.toUpperCase(charArray[i]);
			sb.append(c);
			camel = false ;
		}
		readableString = sb.toString();
	}

	public IExtractTemplate getExtractTemplate() {
		return extractTemplate;
	}

	public void setExtractTemplate(IExtractTemplate extractTemplate) {
		this.extractTemplate = extractTemplate;
	}

	public TemplateElement extract(Token token) throws IOException, LexerException, ParserException {
		if (extractTemplate == null) {
			throw new RuntimeException("Do not know how to extract a template from " + toString());
		}
		return extractTemplate.extract(token);
	}

	@Override
	public String toString() {
		return readableString ;
	}
}
