package com.jaliansystems.simpletemplate.reader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class TokenTest {

	@Test
	public void testCarriesLineNumberInformation() throws IOException,
			LexerException, ParserException {
		StringReader reader = new StringReader(
				"${   {   }$\nsecond.line \"at other position\"\nthird.line");
		TemplateLexer lexer = new TemplateLexer(new LexerReader(reader));

		assertToken(lexer.expect1(TokenType.TT_BLOCK_START),
				TokenType.TT_BLOCK_START, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_CBLOCK_START),
				TokenType.TT_CBLOCK_START, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_BLOCK_END),
				TokenType.TT_BLOCK_END, null, "<stream>", 1);
		assertToken(lexer.expect1(TokenType.TT_IDENTIFIER),
				TokenType.TT_IDENTIFIER, "second.line", "<stream>", 2);
		assertToken(lexer.expect1(TokenType.TT_STRING), TokenType.TT_STRING,
				"at other position", "<stream>", 2);
		assertToken(lexer.expect1(TokenType.TT_IDENTIFIER),
				TokenType.TT_IDENTIFIER, "third.line", "<stream>", 3);
	}

	private void assertToken(Token token, TokenType expectedTokenType,
			String expectedText, String expectedFilename, int expectedLineNumber) {
		assertEquals("Token should match", expectedTokenType, token.getType());
		if (expectedText != null) {
			assertEquals("Token text should match", expectedText,
					token.getValue());
		}
		if (expectedLineNumber != -1)
			assertEquals("Token linenumber should match", expectedLineNumber,
					token.getLineNumber());
		if (expectedFilename != null)
			assertEquals("Token filaname should match", expectedFilename,
					token.getFileName());
	}

}
