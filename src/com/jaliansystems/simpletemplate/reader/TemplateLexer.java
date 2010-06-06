package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public class TemplateLexer extends AbstractLexer {

	public TemplateLexer(LexerReader in) {
		super(in);
		fn = in.getFileName();
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
			} else if (Character.isJavaIdentifierStart(c)) {
				Token token = readIdentifier(c, escape, ln);
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
			} else {
				throw new LexerException(reader.getFileName(),
						reader.getLineNumber(),
						"While reading template start token -- unexpected character '"
								+ (char) c + "'");
			}
		}
		return new Token(TokenType.TT_EOF, fn, ln);
	}

	private Token readIdentifier(int initial, boolean escape, int ln)
			throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		sb.append((char) initial);
		int c;
		while ((c = reader.read()) != -1) {
			if ((c == '.' || Character.isJavaIdentifierPart(c)) && c != '$')
				sb.append((char) c);
			else
				break;
		}
		if (c != -1)
			reader.unread(c);
		String text = sb.toString();
		if (!escape) {
			if ("true".equals(text))
				return new Token(TokenType.TT_TRUE, fn, ln);
			else if ("false".equals(text))
				return new Token(TokenType.TT_FALSE, fn, ln);
			else if ("as".equals(text))
				return new Token(TokenType.TT_AS, fn, ln);
			else if ("to".equals(text))
				return new Token(TokenType.TT_TO, fn, ln);
		}
		return checkValidIdentifier(text) ? new Token(TokenType.TT_IDENTIFIER,
				text, fn, ln) : null;
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

	private boolean checkValidIdentifier(String text) throws LexerException {
		if (text.endsWith(".") || text.contains("..")) {
			throw new LexerException(reader.getFileName(),
					reader.getLineNumber(),
					"While reading template start token -- invalid identifier name '"
							+ text + "'");
		}
		return true;
	}
}
