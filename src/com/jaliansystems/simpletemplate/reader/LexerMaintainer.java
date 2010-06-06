package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

public class LexerMaintainer {
	private ILexer currentLexer ;

	public void pushback(ILexer lexer) throws IOException {
		if (currentLexer != lexer) {
			if (currentLexer != null)
				currentLexer.pushback();
		}
		currentLexer = lexer ;
	}
}
