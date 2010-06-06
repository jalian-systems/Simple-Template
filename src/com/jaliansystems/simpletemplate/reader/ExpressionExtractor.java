package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;

import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;

public class ExpressionExtractor implements ITemplateExtractor {

	@Override
	public TemplateElement extract(Token t, ILexer lexer) throws IOException,
			LexerException, ParserException {
		return createExpression(t, lexer);
	}

	private TemplateElement createExpression(Token t, ILexer lexer)
			throws IOException, LexerException, ParserException {
		VariableTemplate vt = new VariableTemplate(t.getValue(),
				t.getFileName(), t.getLineNumber());
		Token la = lexer.expect1r0(TokenType.TT_OPEN_BR);
		if (la != null) {
			return createIndexedExpression(la, vt, lexer);
		}
		return vt;
	}

	private TemplateElement createIndexedExpression(Token t,
			TemplateElement vt, ILexer lexer) throws LexerException,
			IOException, ParserException {
		while (t != null
				&& (t.getType() == TokenType.TT_OPEN_BR || t.getType() == TokenType.TT_NAME_SEPARATOR)) {
			if (t.getType() == TokenType.TT_OPEN_BR) {
				Token nextToken = lexer.expect1(TokenType.getExtractableTokens(), TokenType.TT_IDENTIFIER);
				TemplateElement index;
				if (nextToken.getType() == TokenType.TT_IDENTIFIER) {
					index = createExpression(nextToken, lexer);
				} else {
					index = nextToken.extract();
				}
				vt = new IndexedAccessTemplate(vt, index, vt.getFileName(),
						vt.getLineNumber());
				lexer.expect1(TokenType.TT_CLOSE_BR);
			} else {
				Token id = lexer.expect1(TokenType.TT_IDENTIFIER);
				vt = new ObjectScopeTemplate(vt, id.getValue(),
						vt.getFileName(), vt.getLineNumber());
			}
			t = lexer.expect1r0(TokenType.TT_OPEN_BR,
					TokenType.TT_NAME_SEPARATOR);
		}
		return vt;
	}

}
