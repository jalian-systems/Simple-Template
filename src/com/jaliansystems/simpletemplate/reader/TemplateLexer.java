package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public class TemplateLexer extends AbstractLexer {

	public TemplateLexer(LexerReader in) {
		super(in);
	}

	public Token getNextToken() throws IOException, LexerException {
		int c;
		boolean escape = false;
		while ((c = reader.read()) != -1) {
			if (Character.isWhitespace(c)) {
				continue;
			} else if (c == '\\') {
				escape = true;
				continue;
			} else if (c == '$') {
				int c1 = reader.read();
				if (c1 == '{')
					return Token.TOK_BLOCK_START;
				if (c1 != -1)
					reader.unread(c1);
				if (c1 == '\\' || Character.isJavaIdentifierStart(c1))
					return readTemplateStartToken();
				return Token.TOK_START;
			} else if (Character.isDigit(c)) {
				return readInteger(c);
			} else if (Character.isJavaIdentifierStart(c)) {
				Token token = readIdentifier(c, escape);
				escape = false;
				return token;
			} else if (c == '"') {
				return readString();
			} else if (c == '}') {
				int la = reader.read();
				if (la == '$')
					return Token.TOK_BLOCK_END;
				throw new LexerException(reader.getFileName(), reader.getLineNumber(),
						"While reading template start token -- unexpected character '"
								+ (char) c + "'");
			} else if (c == '{') {
				return Token.TOK_CBLOCK_START;
			} else if (c == '[') {
				return Token.TOK_OPEN_BR;
			} else if (c == ']') {
				return Token.TOK_CLOSE_BR;
			} else if (c == '.') {
				return Token.TOK_NAME_SEPARATOR ;
			} else {
				throw new LexerException(reader.getFileName(), reader.getLineNumber(),
						"While reading template start token -- unexpected character '"
								+ (char) c + "'");
			}
		}
		return Token.TOK_EOF;
	}

	private Token readIdentifier(int initial, boolean escape)
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
				return Token.TOK_TRUE;
			else if ("false".equals(text))
				return Token.TOK_FALSE;
			else if ("as".equals(text))
				return Token.TOK_AS;
			else if ("to".equals(text))
				return Token.TOK_TO;
		}
		return checkValidIdentifier(text) ? new Token(TokenType.TT_IDENTIFIER,
				text) : null;
	}

	private Token readInteger(int initial) throws IOException {
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
		return new Token(TokenType.TT_INTEGER, sb.toString());
	}

	private Token readString() throws IOException, LexerException {
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
				return new Token(TokenType.TT_STRING, sb.toString());
			}
			sb.append((char) c);
		}
		throw new LexerException(reader.getFileName(), reader.getLineNumber(), "While reading string - unexpected EOF");
	}

	private Token readTemplateStartToken() throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		int c;
		boolean start = true;
		boolean escape = false;
		while ((c = reader.read()) != -1) {
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
			if (c != -1) {
				reader.unread(c);
			}
			return findToken(sb.toString(), escape);
		}
		throw new LexerException(reader.getFileName(), reader.getLineNumber(),
				"While reading template start token -- unexpected "
						+ (c == -1 ? "EOF" : "character '" + (char) c + "'"));
	}

	private Token findToken(String text, boolean escape) throws LexerException {
		if (!escape) {
			if ("if".equals(text))
				return Token.TOK_IF;
			else if ("ifelse".equals(text))
				return Token.TOK_IFELSE;
			else if ("set".equals(text))
				return Token.TOK_SET;
			else if ("with".equals(text))
				return Token.TOK_WITH;
		}
		return checkValidIdentifier(text) ? new Token(
				TokenType.TT_START_IDENTIFIER, text) : null;
	}

	private boolean checkValidIdentifier(String text) throws LexerException {
		if (text.endsWith(".") || text.contains("..")) {
			throw new LexerException(reader.getFileName(), reader.getLineNumber(),
					"While reading template start token -- invalid identifier name '"
							+ text + "'");
		}
		return true;
	}
}
