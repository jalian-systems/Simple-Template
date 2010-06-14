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
import java.util.Arrays;
import java.util.List;

public abstract class AbstractLexer implements ILexer {

	private Token laToken;
	protected final LexerReader reader;
	protected String fn;
	protected String ttStart;
	protected String ttEnd;
	private final LexerMaintainer maintainer;

	public AbstractLexer(LexerReader in, LexerMaintainer maintainer) {
		this.reader = in;
		this.maintainer = maintainer;
		ttStart = in.getTokenStart();
		ttEnd = in.getTokenEnd();
	}

	public LexerReader getReader() {
		return reader;
	}

	protected abstract Token getNextToken() throws IOException, LexerException;

	public final Token nextToken() throws IOException, LexerException {
		Token token;
		if (laToken == null)
			token = getNextToken();
		else
			token = laToken;
		laToken = null;
		return token;
	}

	public final Token lookAhead() throws IOException, LexerException {
		if (laToken != null)
			return laToken;
		reader.mark();
		laToken = getNextToken();
		return laToken;
	}

	@Override
	public void pushbackLACharacters() throws IOException {
		if (laToken != null) {
			reader.reset();
			laToken = null;
		}
	}

	public Token expect1(TokenType[] types, TokenType... others)
			throws IOException, LexerException, ParserException {
		Token t = expect1r0(types, others);
		if (t == null) {
			t = nextToken();
			List<TokenType> asList = new ArrayList<TokenType>(
					Arrays.asList(types));
			asList.addAll(Arrays.asList(others));
			throw new ParserException(reader.getFileName(),
					reader.getLineNumber(), "Expecting one of " + asList
							+ " Got: " + t);
		}
		return t;
	}

	public Token expect1r0(TokenType[] types, TokenType... others)
			throws IOException, LexerException, ParserException {
		if (maintainer != null)
			maintainer.reset(this);
		Token la = lookAhead();
		Token t = findMatchingToken(la, types);
		if (t == null)
			return findMatchingToken(la, others);
		return t;
	}

	private Token findMatchingToken(Token la, TokenType[] types)
			throws IOException, LexerException {
		for (int i = 0; i < types.length; i++) {
			if (la.getType() == types[i]) {
				return nextToken();
			}
			if (types[i] == TokenType.TT_ALIAS
					&& la.getType() == TokenType.TT_IDENTIFIER) {
				String value = la.getValue();
				if (!value.contains(".")) {
					nextToken();
					return new Token(TokenType.TT_ALIAS, la.getValue(),
							la.getFileName(), la.getLineNumber());
				}
			}
		}
		return null;
	}

	public Token expect1(TokenType... types) throws IOException,
			LexerException, ParserException {
		return expect1(new TokenType[0], types);
	}

	public Token expect1r0(TokenType... types) throws IOException,
			LexerException, ParserException {
		return expect1r0(new TokenType[0], types);
	}

	protected void skipRestOfStartOfTemplate() throws IOException {
		skipRest(ttStart);
	}

	protected boolean isStartOfTemplate(int c) throws IOException {
		return isStart(ttStart, c);
	}

	protected void skipRestOfEndOfTemplate() throws IOException {
		skipRest(ttEnd);
	}

	protected boolean isEndOfTemplate(int c) throws IOException {
		return isStart(ttEnd, c);
	}

	private boolean isStart(String s, int c) throws IOException {
		if (c != s.charAt(0))
			return false;
		else if (s.length() == 1)
			return true;
		char[] la = new char[s.length() - 1];
		int len = reader.read(la);
		reader.unread(Arrays.copyOfRange(la, 0, len));
		return len < la.length || s.substring(1).equals(new String(la, 0, len));
	}

	private void skipRest(String s) throws IOException {
		for (int i = 0; i < s.length() - 1; i++)
			reader.read();
	}

	protected Token readTemplateStartToken(int ln, boolean throwException)
			throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		int c;
		skipRestOfStartOfTemplate();
		boolean start = true;
		boolean escape = false;
		boolean methodCall = false;
		while ((c = reader.read()) != -1) {
			if (start) {
				if (c == '{')
					return new Token(TokenType.TT_BLOCK_START, fn, ln);
			}
			if (start && c == ':') {
				methodCall = true;
				continue;
			}
			if (start && c == '\\') {
				escape = true;
				continue;
			}
			if (start) {
				start = false;
				if (Character.isJavaIdentifierStart(c) && !isStartOfTemplate(c))
					sb.append((char) c);
				else
					break;
			} else {
				if ((c == '.' || Character.isJavaIdentifierPart(c))
						&& !isStartOfTemplate(c))
					sb.append((char) c);
				else
					break;
			}
		}
		if (sb.length() > 0) {
			if (c != -1) {
				reader.unread(c);
			}
			return findToken(sb.toString(), methodCall, escape, ln);
		}
		if (throwException)
			throw new LexerException(
					reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- unexpected "
							+ (c == -1 ? "EOF" : "character '" + (char) c + "'"));
		if (c != -1) {
			reader.unread(c);
			if (methodCall)
				reader.unread(':');
		} else if (methodCall)
			reader.unread(':');
		return null;
	}

	private Token findToken(String text, boolean methodCall, boolean escape,
			int ln) throws LexerException {
		if (methodCall)
			return checkValidMethodCall(text, ln);
		if (!escape) {
			if ("if".equals(text))
				return new Token(TokenType.TT_IF, fn, ln);
			else if ("ifelse".equals(text))
				return new Token(TokenType.TT_IFELSE, fn, ln);
			else if ("set".equals(text))
				return new Token(TokenType.TT_SET, fn, ln);
			else if ("include".equals(text))
				return new Token(TokenType.TT_INCLUDE, fn, ln);
			else if ("with".equals(text))
				return new Token(TokenType.TT_WITH, fn, ln);
		}
		return checkValidIdentifier(text, ln);
	}

	private Token checkValidMethodCall(String text, int ln)
			throws LexerException {
		if (text.contains(".")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid method name '"
							+ text + "'");
		}
		return new Token(TokenType.TT_START_METHOD_CALL, text, fn, ln);
	}

	protected Token checkValidIdentifier(String text, int ln)
			throws LexerException {
		if (text.endsWith(".") || text.contains("..")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid identifier name '"
							+ text + "'");
		}
		return new Token(TokenType.TT_START_IDENTIFIER, text, fn, ln);
	}

	protected Token checkValidIdentifier(String text, int ln, TokenType t)
			throws LexerException {
		if (text.endsWith(".") || text.contains("..")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid identifier name '"
							+ text + "'");
		}
		return new Token(t, text, fn, ln);
	}

	public String getEndToken() {
		return ttEnd;
	}

	public String getStartToken() {
		return ttStart;
	}

}
