package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public class TextLexer extends AbstractLexer {

	private String fn;

	public TextLexer(LexerReader in) {
		super(in);
		fn = in.getFileName();
	}

	public Token getNextToken() throws IOException, LexerException {
		int c;
		boolean escape = false;
		StringBuffer sb = new StringBuffer();
		int ln ;
		while ((ln = reader.getLineNumber()) != -1 && (c = reader.read()) != -1) {
			if (escape) {
				if (c != '\n') {
					if (c == 'n')
						c = '\n';
					sb.append((char) c);
				}
				escape = false;
				continue;
			}
			if (c == '\\') {
				escape = true;
				continue;
			}
			if (c == '$' && sb.length() > 0) {
				reader.unread(c);
				return new Token(TokenType.TT_TEXT, sb.toString(), fn, ln);
			}
			if (c == '$') {
				return readTemplateStartToken(ln);
			}
			if (c == '}') {
				int la = reader.read();
				if (sb.length() > 0) {
					if (la == '$') {
						reader.unread(la);
						reader.unread(c);
						return new Token(TokenType.TT_TEXT, sb.toString(), fn, ln);
					}
					reader.unread(la);
				} else {
					if (la == '$') {
						return new Token(TokenType.TT_BLOCK_END, fn, ln);
					}
					reader.unread(la);
				}
			}
			sb.append((char) c);
		}
		if (sb.length() > 0)
			return new Token(TokenType.TT_TEXT, sb.toString(), fn, ln);
		return new Token(TokenType.TT_EOF, fn, ln);
	}

	private Token readTemplateStartToken(int ln) throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		int c;
		boolean start = true;
		boolean escape = false;
		while ((c = reader.read()) != -1) {
			if (start) {
				if (c == '{')
					return new Token(TokenType.TT_BLOCK_START, fn, ln);
			}
			if (start && c == '\\') {
				escape = true;
				continue;
			}
			if (start) {
				start = false;
				if (Character.isJavaIdentifierStart(c) && c != '$')
					sb.append((char) c);
				else
					break;
			} else {
				if ((c == '.' || Character.isJavaIdentifierPart(c)) && c != '$')
					sb.append((char) c);
				else
					break;
			}
		}
		if (sb.length() > 0) {
			if (c != -1)
				reader.unread(c);
			return findToken(sb.toString(), escape, ln);
		}
		throw new LexerException(reader.getFileName(), reader.getLineNumber(),
				"While reading template start token -- unexpected "
						+ (c == -1 ? "EOF" : "character '" + (char) c + "'"));
	}

	private Token findToken(String text, boolean escape, int ln) throws LexerException {
		if (!escape) {
			if ("if".equals(text))
				return new Token(TokenType.TT_IF, fn, ln);
			else if ("ifelse".equals(text))
				return new Token(TokenType.TT_IFELSE, fn, ln);
			else if ("set".equals(text))
				return new Token(TokenType.TT_SET, fn, ln);
			else if ("with".equals(text))
				return new Token(TokenType.TT_WITH, fn, ln);
		}
		return checkValidIdentifier(text, ln);
	}

	private Token checkValidIdentifier(String text, int ln) throws LexerException {
		if (text.endsWith(".") || text.contains("..")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid identifier name '"
							+ text + "'");
		}
		return new Token(TokenType.TT_START_IDENTIFIER, text, fn, ln);
	}
}
