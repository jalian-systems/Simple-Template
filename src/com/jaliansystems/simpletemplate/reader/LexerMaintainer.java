package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LexerMaintainer {
	private Map<LexerReader, ILexer> readerLexerMap = new HashMap<LexerReader, ILexer>();

	public void reset(AbstractLexer lexer) throws IOException {
		ILexer currentLexer = readerLexerMap.get(lexer.getReader());
		if (currentLexer != lexer) {
			if (currentLexer != null)
				currentLexer.pushbackLACharacters();
		}
		readerLexerMap.put(lexer.getReader(), lexer) ;
	}

	public void remove(AbstractLexer lexer) {
		readerLexerMap.remove(lexer.getReader()) ;
	}
}
