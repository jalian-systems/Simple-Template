package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import com.jaliansystems.simpletemplate.templates.CompositeTemplate;
import com.jaliansystems.simpletemplate.templates.IfTemplate;
import com.jaliansystems.simpletemplate.templates.IndexedAccessTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralBooleanTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralIntegerTemplate;
import com.jaliansystems.simpletemplate.templates.LiteralTextTemplate;
import com.jaliansystems.simpletemplate.templates.LoopTemplate;
import com.jaliansystems.simpletemplate.templates.ObjectScopeTemplate;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.VariableScopeTemplate;
import com.jaliansystems.simpletemplate.templates.VariableTemplate;
import com.jaliansystems.simpletemplate.templates.WithTemplate;

public class TemplateReader implements ITemplateReader {

	private static final TokenType[] TEXT_LEXER_TYPES = new TokenType[] {
			TokenType.TT_TEXT, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_EOF };

	private static final TokenType[] CBLOCK_START_TYPES = new TokenType[] {
			TokenType.TT_TEXT, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_BLOCK_END };

	private static final TokenType[] BLOCK_START_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_BLOCK_END };

	private static final TokenType[] START_ID_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_START,
			TokenType.TT_CBLOCK_START };

	private static final TokenType[] IF_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_START,
			TokenType.TT_IDENTIFIER };

	private static final TokenType[] IF_TEMPLATE_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_START,
			TokenType.TT_CBLOCK_START };

	private static final TokenType[] WITH_TEMPLATE_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_START,
			TokenType.TT_CBLOCK_START };

	private final LexerReader in;
	private final TextLexer textLexer;
	private final TemplateLexer templateLexer;
	private ILexer currentLexer;

	public TemplateReader(Reader in) {
		this(in, null);
	}

	public TemplateReader(Reader in, String fileName) {
		this.in = new LexerReader(in, fileName);
		textLexer = new TextLexer(this.in);
		templateLexer = new TemplateLexer(this.in);
		currentLexer = textLexer;
		TokenType.TT_BLOCK_START.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createBlockTemplateWithTemplateLexer(BLOCK_START_TYPES);
			}
		});
		TokenType.TT_START_IDENTIFIER
				.setExtractTemplate(new IExtractTemplate() {
					@Override
					public TemplateElement extract(Token t) throws IOException,
							LexerException, ParserException {
						return createStartIdTemplate(t);
					}
				});
		TokenType.TT_TEXT.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralTextTemplate(t.getValue());
			}
		});
		TokenType.TT_STRING.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralTextTemplate(t.getValue());
			}
		});
		TokenType.TT_INTEGER.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralIntegerTemplate(
						Integer.parseInt(t.getValue()));
			}
		});
		TokenType.TT_TRUE.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralBooleanTemplate(t == Token.TOK_TRUE);
			}
		});
		TokenType.TT_FALSE.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralBooleanTemplate(t == Token.TOK_TRUE);
			}
		});
		TokenType.TT_IF.setExtractTemplate(new IExtractTemplate() {
			@Override
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createIfTemplate(t);
			}
		});
		TokenType.TT_IFELSE.setExtractTemplate(new IExtractTemplate() {
			@Override
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createIfTemplate(t);
			}
		});
		TokenType.TT_WITH.setExtractTemplate(new IExtractTemplate() {
			@Override
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createWithTemplate(t);
			}
		});
		TokenType.TT_SET.setExtractTemplate(new IExtractTemplate() {
			@Override
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createSetTemplate(t);
			}
		});
		TokenType.TT_CBLOCK_START.setExtractTemplate(new IExtractTemplate() {
			@Override
			public TemplateElement extract(Token t) throws IOException,
					LexerException, ParserException {
				return createBlockTemplateWithTextLexer(CBLOCK_START_TYPES);
			}
		});
	}

	private ILexer getTextLexer() throws IOException {
		if (currentLexer != textLexer)
			currentLexer.pushback();
		currentLexer = textLexer;
		return textLexer;
	}

	private ILexer getTemplateLexer() throws IOException {
		if (currentLexer != templateLexer)
			currentLexer.pushback();
		currentLexer = templateLexer;
		return templateLexer;
	}

	@Override
	public TemplateElement readTemplate() throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate();
		Token t;
		while ((t = expect1(getTextLexer(), TEXT_LEXER_TYPES)) != Token.TOK_EOF) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createStartIdTemplate(Token t) throws IOException,
			LexerException, ParserException {
		TemplateElement ite;
		TemplateElement vt = createExpression(t.getValue());
		Token nextToken = expect1(getTemplateLexer(), START_ID_TYPES);
		if (nextToken == Token.TOK_START) {
			ite = vt;
		} else {
			ite = new LoopTemplate(vt, nextToken.extract());
		}
		return ite;
	}

	private TemplateElement createBlockTemplateWithTemplateLexer(
			TokenType[] expectedTypes) throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate();
		Token t;
		while ((t = expect1(getTemplateLexer(), expectedTypes)) != Token.TOK_BLOCK_END) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createBlockTemplateWithTextLexer(
			TokenType[] expectedTypes) throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate();
		Token t;
		while ((t = expect1(getTextLexer(), expectedTypes)) != Token.TOK_BLOCK_END) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createExpression(String variable)
			throws IOException, LexerException, ParserException {
		VariableTemplate vt = new VariableTemplate(variable);
		Token la = getTemplateLexer().lookAhead();
		if (la == Token.TOK_OPEN_BR) {
			return createIndexedExpression(vt);
		}
		return vt;
	}

	private TemplateElement createIndexedExpression(TemplateElement vt)
			throws LexerException, IOException, ParserException {
		Token t = expect1r0(getTemplateLexer(), TokenType.TT_OPEN_BR,
				TokenType.TT_NAME_SEPARATOR);
		while (t == Token.TOK_OPEN_BR || t == Token.TOK_NAME_SEPARATOR) {
			if (t == Token.TOK_OPEN_BR) {
				Token nextToken = getTemplateLexer().nextToken();
				TemplateElement index;
				if (nextToken.getType() == TokenType.TT_IDENTIFIER) {
					index = createExpression(nextToken.getValue());
				} else {
					index = nextToken.extract();
				}
				vt = new IndexedAccessTemplate(vt, index);
				expect1(getTemplateLexer(), TokenType.TT_CLOSE_BR);
			} else {
				Token id = expect1(getTemplateLexer(), TokenType.TT_IDENTIFIER);
				vt = new ObjectScopeTemplate(vt, id.getValue());
			}
			t = expect1r0(getTemplateLexer(), TokenType.TT_OPEN_BR,
					TokenType.TT_NAME_SEPARATOR);
		}
		return vt;
	}

	private TemplateElement createIfTemplate(Token tokenGot)
			throws IOException, LexerException, ParserException {
		Token t = expect1(getTemplateLexer(), IF_TYPES);
		TemplateElement condition;
		if (t.getType() == TokenType.TT_IDENTIFIER) {
			condition = createExpression(t.getValue());
		} else {
			condition = t.extract();
		}
		t = expect1(getTemplateLexer(), IF_TEMPLATE_TYPES);
		TemplateElement trueBranch = t.extract();
		TemplateElement falseBranch = null;
		if (tokenGot == Token.TOK_IFELSE) {
			t = expect1(getTemplateLexer(), IF_TEMPLATE_TYPES);
			falseBranch = t.extract();
		}
		return new IfTemplate(condition, trueBranch, falseBranch);
	}

	private TemplateElement createWithTemplate(Token t) throws IOException,
			LexerException, ParserException {
		Token next = expect1(getTemplateLexer(), TokenType.TT_IDENTIFIER);
		TemplateElement withVar = createExpression(next.getValue());
		next = expect1r0(getTemplateLexer(), TokenType.TT_AS);
		String alias = null;
		if (next != null) {
			next = expect1(getTemplateLexer(), TokenType.TT_ALIAS);
			alias = next.getValue();
		}
		next = expect1(getTemplateLexer(), WITH_TEMPLATE_TYPES);
		TemplateElement template = next.extract();
		return new WithTemplate(withVar, alias, template);
	}

	private TemplateElement createSetTemplate(Token t) throws IOException,
			LexerException, ParserException {
		Token next = expect1(getTemplateLexer(), TokenType.TT_ALIAS);
		String alias = next.getValue();
		expect1(getTemplateLexer(), TokenType.TT_TO);
		next = expect1(getTemplateLexer(), TokenType.TT_IDENTIFIER);
		TemplateElement setVar = createExpression(next.getValue());
		return new VariableScopeTemplate(setVar, alias);
	}

	private Token expect1(ILexer lexer, TokenType... types) throws IOException,
			LexerException, ParserException {
		Token t = expect1r0(lexer, types);
		if (t == null) {
			t = lexer.nextToken();
			throw new ParserException(in.getFileName(), in.getLineNumber(),
					"Expecting one of " + Arrays.asList(types) + " Got: " + t);
		}
		return t;
	}

	private Token expect1r0(ILexer lexer, TokenType... types)
			throws IOException, LexerException {
		Token la = lexer.lookAhead();
		for (int i = 0; i < types.length; i++) {
			if (la.getType() == types[i]) {
				return lexer.nextToken();
			}
			if (types[i] == TokenType.TT_ALIAS
					&& la.getType() == TokenType.TT_IDENTIFIER) {
				String value = la.getValue();
				if (!value.contains(".")) {
					lexer.nextToken();
					return new Token(TokenType.TT_ALIAS, la.getValue());
				}
			}
		}
		return null;
	}
}
