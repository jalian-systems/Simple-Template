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

import java.io.IOException;
import java.util.ArrayList;

import com.jaliansystems.simpletemplate.templates.TemplateElement;

public enum TokenType {
	TT_END_TEMPLATE,
	TT_WITH(new WithTExtractor()),
	TT_SET(new SetTExtractor()),
	TT_IF(new IfElseTExtractor()),
	TT_IFELSE(new IfElseTExtractor()),
	TT_BLOCK_END,
	TT_OPEN_BR,
	TT_CLOSE_BR,
	TT_NAME_SEPARATOR,
	TT_TO,
	TT_AS,
	TT_TRUE(new LBTExtractor()),
	TT_FALSE(new LBTExtractor()),
	TT_START_IDENTIFIER(new StartIDExtractor()),
	TT_IDENTIFIER,
	TT_INTEGER(new LITExtractor()),
	TT_STRING(new LTTExtractor()),
	TT_BOOLEAN,
	TT_TEXT(new LTTExtractor()),
	TT_EOF,
	TT_CBLOCK_START(new BTExtractor()),
	TT_BLOCK_START(new BTExtractor()),
	TT_ALIAS,
	TT_CLOSE_PAREN,
	TT_OPEN_PAREN,
	TT_COMMA,
	TT_METHOD_CALL(new MethodCallExtractor()),
	TT_INCLUDE(new IncludeTemplateExtractor());
	
	private static TokenType[] extractableTokenTypes;
	
	private ITemplateExtractor extractor ;
	private String readableString ;
	private ILexer lexer;
	
	TokenType() {
		this(null);
	}
	
	TokenType(ITemplateExtractor extractor) {
		this.extractor = extractor;
		setUpReadableString();
	}
	
	public boolean isExtractable() {
		return extractor != null ;
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

	public ITemplateExtractor getExtractTemplate() {
		return extractor;
	}

	public void setExtractTemplate(ITemplateExtractor extractTemplate) {
		this.extractor = extractTemplate;
	}

	public TemplateElement extract(Token token) throws IOException, LexerException, ParserException {
		if (extractor == null) {
			throw new RuntimeException("Do not know how to extract a template from " + toString());
		}
		return extractor.extract(token, lexer);
	}

	@Override
	public String toString() {
		return readableString ;
	}

	public void setLexer(ILexer lexer) {
		this.lexer = lexer;
	}

	public static TokenType[] getExtractableTokens() {
		if (extractableTokenTypes == null) {
			ArrayList<TokenType> extractableList = new ArrayList<TokenType>();
			for (TokenType tokenType : TokenType.values()) {
				if (tokenType.isExtractable())
					extractableList.add(tokenType);
			}
			extractableTokenTypes = extractableList
					.toArray(new TokenType[extractableList.size()]);
		}
		return extractableTokenTypes;
	}
}
