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

public class TemplateLexer extends AbstractLexer {

	public TemplateLexer(LexerReader in, LexerMaintainer maintainer) {
		super(in, maintainer);
		fn = in.getFileName();
	}

	public TemplateLexer(LexerReader in) {
		this(in, null);
	}

	public Token getNextToken() throws IOException, LexerException {
		int c;
		int ln;
		boolean escape = false;
		while ((ln = reader.getLineNumber()) > 0 && (c = reader.read()) != -1) {
			if (Character.isWhitespace(c)) {
				continue;
			} else if (c == '\\') {
				escape = true;
				continue;
			} else if (isStartOfTemplate(c)) {
				Token token = readTemplateStartToken(ln, !ttStart.equals(ttEnd));
				if (token == null)
					token = new Token(TokenType.TT_END_TEMPLATE, fn, ln);
				return token;
			} else if (isEndOfTemplate(c)) {
				skipRestOfEndOfTemplate();
				return new Token(TokenType.TT_END_TEMPLATE, fn, ln);
			} else if (Character.isDigit(c)) {
				return readInteger(c, ln);
			} else if (c == ':' || Character.isJavaIdentifierStart(c)) {
				reader.unread(c);
				Token token = readIdentifier(escape, ln);
				escape = false;
				return token;
			} else if (c == '"') {
				return readString(ln);
			} else if (c == '}') {
				int la = reader.read();
				if (isEndOfTemplate(la)) {
					skipRestOfEndOfTemplate();
					return new Token(TokenType.TT_BLOCK_END, fn, ln);
				}
				reader.unread(la);
			} else if (c == '{') {
				return new Token(TokenType.TT_CBLOCK_START, fn, ln);
			} else if (c == '[') {
				return new Token(TokenType.TT_OPEN_BR, fn, ln);
			} else if (c == ']') {
				return new Token(TokenType.TT_CLOSE_BR, fn, ln);
			} else if (c == '.') {
				return new Token(TokenType.TT_NAME_SEPARATOR, fn, ln);
			} else if (c == '(') {
				return new Token(TokenType.TT_OPEN_PAREN, fn, ln);
			} else if (c == ')') {
				return new Token(TokenType.TT_CLOSE_PAREN, fn, ln);
			} else if (c == ',') {
				return new Token(TokenType.TT_COMMA, fn, ln);
			} else {
				throw new LexerException(reader.getFileName(),
						reader.getLineNumber(),
						"While reading template start token -- unexpected character '"
								+ (char) c + "'");
			}
		}
		return new Token(TokenType.TT_EOF, fn, ln);
	}

	private Token readIdentifier(boolean escape, int ln) throws IOException,
			LexerException {
		StringBuffer sb = new StringBuffer();
		int c;
		boolean methodCall = false;
		boolean start = true;
		while ((c = reader.read()) != -1) {
			if (start) {
				if (c == ':') {
					methodCall = true;
					continue;
				}
				start = false;
			}
			if ((c == '.' || Character.isJavaIdentifierPart(c)) && c != '$')
				sb.append((char) c);
			else
				break;
		}
		if (c != -1)
			reader.unread(c);
		String text = sb.toString();
		if (methodCall)
			return checkValidMethodCall(text, ln);
		if (!escape) {
			if ("true".equals(text))
				return new Token(TokenType.TT_TRUE, fn, ln);
			else if ("false".equals(text))
				return new Token(TokenType.TT_FALSE, fn, ln);
			else if ("as".equals(text))
				return new Token(TokenType.TT_AS, fn, ln);
			else if ("with".equals(text))
				return new Token(TokenType.TT_WITH, fn, ln);
			else if ("set".equals(text))
				return new Token(TokenType.TT_SET, fn, ln);
			else if ("if".equals(text))
				return new Token(TokenType.TT_IF, fn, ln);
			else if ("ifelse".equals(text))
				return new Token(TokenType.TT_IFELSE, fn, ln);
			else if ("include".equals(text))
				return new Token(TokenType.TT_INCLUDE, fn, ln);
			else if ("to".equals(text))
				return new Token(TokenType.TT_TO, fn, ln);
		}
		return checkValidIdentifier(text, ln, TokenType.TT_IDENTIFIER);
	}

	private Token checkValidMethodCall(String text, int ln)
			throws LexerException {
		if (text.contains(".")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid method name '"
							+ text + "'");
		}
		return new Token(TokenType.TT_METHOD_CALL, text, fn, ln);
	}

	private Token readInteger(int initial, int ln) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append((char) initial);
		int c;
		while ((c = reader.read()) != -1) {
			if (Character.isDigit(c))
				sb.append((char) c);
			else
				break;
		}
		if (c != -1)
			reader.unread(c);
		return new Token(TokenType.TT_INTEGER, sb.toString(), fn, ln);
	}

	private Token readString(int ln) throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		int c;
		boolean escape = false;
		while ((c = reader.read()) != -1) {
			if (c == '\\') {
				escape = true;
				continue;
			}
			if (escape) {
				if (c == 'n')
					sb.append('\n');
				else
					sb.append((char) c);
				escape = false;
				continue;
			}
			if (c == '"') {
				return new Token(TokenType.TT_STRING, sb.toString(), fn, ln);
			}
			sb.append((char) c);
		}
		throw new LexerException(reader.getFileName(), reader.getLineNumber(),
				"While reading string - unexpected EOF");
	}
}
