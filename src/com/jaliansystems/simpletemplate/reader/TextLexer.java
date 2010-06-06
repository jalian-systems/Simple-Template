package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public class TextLexer extends AbstractLexer {

	public TextLexer(LexerReader in, LexerMaintainer maintainer) {
		super(in, maintainer);
		fn = in.getFileName();
	}

	public TextLexer(LexerReader in) {
		this(in, null);
	}

	public Token getNextToken() throws IOException, LexerException {
		int c;
		int ln;
		while ((ln = reader.getLineNumber()) != -1 && (c = reader.read()) != -1) {
			if (isStartOfTemplate(c)) {
				return readTemplateStartToken(ln, true);
			} else if (c == '}') {
				int la = reader.read();
				if (isEndOfTemplate(la)) {
					skipRestOfEndOfTemplate();
					return new Token(TokenType.TT_BLOCK_END, fn, ln);
				}
				reader.unread(la);
			}
			reader.unread(c);
			return readTextToken();

		}
		return new Token(TokenType.TT_EOF, fn, ln);
	}

	private Token readTextToken() throws IOException, LexerException {
		StringBuffer sb = new StringBuffer();
		int ln;
		int c = -1;
		boolean escape = false;
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
			if (isStartOfTemplate(c))
				break ;
			if (c == '}') {
				int la = reader.read();
				if (la != -1)
					reader.unread(la);
				if (isEndOfTemplate(la)) {
					break ;
				}
			}
			sb.append((char) c);
		}
		if (sb.length() > 0) {
			if (c != -1)
				reader.unread(c);
			return new Token(TokenType.TT_TEXT, sb.toString(), fn, ln);
		}
		throw new LexerException(reader.getFileName(), reader.getLineNumber(),
				"While reading template start token -- unexpected "
						+ (c == -1 ? "EOF" : "character '" + (char) c + "'"));
	}
}
