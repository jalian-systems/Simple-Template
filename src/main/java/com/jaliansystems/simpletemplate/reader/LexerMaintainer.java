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
