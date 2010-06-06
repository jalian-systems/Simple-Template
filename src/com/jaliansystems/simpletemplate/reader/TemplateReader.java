package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.Reader;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.Log;
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
			TokenType.TT_BLOCK_START, TokenType.TT_END_TEMPLATE,
			TokenType.TT_CBLOCK_START };

	private static final TokenType[] IF_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_END_TEMPLATE,
			TokenType.TT_IDENTIFIER };

	private static final TokenType[] IF_TEMPLATE_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_END_TEMPLATE,
			TokenType.TT_CBLOCK_START };

	private static final TokenType[] WITH_TEMPLATE_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_END_TEMPLATE,
			TokenType.TT_CBLOCK_START };

	private static final TokenType[] OPEN_BR_TYPES = new TokenType[] {
			TokenType.TT_STRING, TokenType.TT_INTEGER, TokenType.TT_TRUE,
			TokenType.TT_FALSE, TokenType.TT_START_IDENTIFIER, TokenType.TT_IF,
			TokenType.TT_IFELSE, TokenType.TT_WITH, TokenType.TT_SET,
			TokenType.TT_BLOCK_START, TokenType.TT_END_TEMPLATE,
			TokenType.TT_IDENTIFIER };

	private final LexerReader in;
	private final AbstractLexer textLexer;
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
				return new LiteralTextTemplate(t.getValue(), t.getFileName(), t.getLineNumber());
			}
		});
		TokenType.TT_STRING.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralTextTemplate(t.getValue(), t.getFileName(), t.getLineNumber());
			}
		});
		TokenType.TT_INTEGER.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralIntegerTemplate(
						Integer.parseInt(t.getValue()), t.getFileName(), t.getLineNumber());
			}
		});
		TokenType.TT_TRUE.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralBooleanTemplate(
						t.getType() == TokenType.TT_TRUE, t.getFileName(), t.getLineNumber());
			}
		});
		TokenType.TT_FALSE.setExtractTemplate(new IExtractTemplate() {
			public TemplateElement extract(Token t) {
				return new LiteralBooleanTemplate(
						t.getType() == TokenType.TT_TRUE, t.getFileName(), t.getLineNumber());
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

	public void setEvaluationMode(EvaluationMode mode) {
		Log.setMode(mode);
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
		CompositeTemplate ct = new CompositeTemplate(in.getFileName(), in.getLineNumber());
		Token t;
		while ((t = getTextLexer().expect1(TEXT_LEXER_TYPES)).getType() != TokenType.TT_EOF) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createStartIdTemplate(Token t) throws IOException,
			LexerException, ParserException {
		TemplateElement ite;
		TemplateElement vt = createExpression(t);
		Token nextToken = getTemplateLexer().expect1(START_ID_TYPES);
		if (nextToken.getType() == TokenType.TT_END_TEMPLATE) {
			ite = vt;
		} else {
			TemplateElement template = nextToken.extract();
			ite = new LoopTemplate(vt, template, t.getFileName(), t.getLineNumber());
		}
		return ite;
	}

	private TemplateElement createBlockTemplateWithTemplateLexer(
			TokenType[] expectedTypes) throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate(in.getFileName(), in.getLineNumber());
		Token t;
		while ((t = getTemplateLexer().expect1(expectedTypes)).getType() != TokenType.TT_BLOCK_END) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createBlockTemplateWithTextLexer(
			TokenType[] expectedTypes) throws IOException, LexerException,
			ParserException {
		CompositeTemplate ct = new CompositeTemplate(in.getFileName(), in.getLineNumber());
		Token t;
		while ((t = getTextLexer().expect1(expectedTypes)).getType() != TokenType.TT_BLOCK_END) {
			ct.add(t.extract());
		}
		return ct;
	}

	private TemplateElement createExpression(Token t)
			throws IOException, LexerException, ParserException {
		VariableTemplate vt = new VariableTemplate(t.getValue(), t.getFileName(), t.getLineNumber());
		Token la = getTemplateLexer().lookAhead();
		if (la.getType() == TokenType.TT_OPEN_BR) {
			return createIndexedExpression(vt);
		}
		return vt;
	}

	private TemplateElement createIndexedExpression(TemplateElement vt)
			throws LexerException, IOException, ParserException {
		Token t = getTemplateLexer().expect1r0(TokenType.TT_OPEN_BR,
				TokenType.TT_NAME_SEPARATOR);
		while (t != null
				&& (t.getType() == TokenType.TT_OPEN_BR || t.getType() == TokenType.TT_NAME_SEPARATOR)) {
			if (t.getType() == TokenType.TT_OPEN_BR) {
				Token nextToken = templateLexer.expect1(OPEN_BR_TYPES);
				TemplateElement index;
				if (nextToken.getType() == TokenType.TT_IDENTIFIER) {
					index = createExpression(nextToken);
				} else {
					index = nextToken.extract();
				}
				vt = new IndexedAccessTemplate(vt, index, vt.getFileName(), vt.getLineNumber());
				getTemplateLexer().expect1(TokenType.TT_CLOSE_BR);
			} else {
				Token id = getTemplateLexer().expect1(TokenType.TT_IDENTIFIER);
				vt = new ObjectScopeTemplate(vt, id.getValue(), vt.getFileName(), vt.getLineNumber());
			}
			t = getTemplateLexer().expect1r0(TokenType.TT_OPEN_BR,
					TokenType.TT_NAME_SEPARATOR);
		}
		return vt;
	}

	private TemplateElement createIfTemplate(Token tokenGot)
			throws IOException, LexerException, ParserException {
		Token t = getTemplateLexer().expect1(IF_TYPES);
		TemplateElement condition;
		if (t.getType() == TokenType.TT_IDENTIFIER) {
			condition = createExpression(t);
		} else {
			condition = t.extract();
		}
		t = getTemplateLexer().expect1(IF_TEMPLATE_TYPES);
		TemplateElement trueBranch = t.extract();
		TemplateElement falseBranch = null;
		if (tokenGot.getType() == TokenType.TT_IFELSE) {
			t = getTemplateLexer().expect1(IF_TEMPLATE_TYPES);
			falseBranch = t.extract();
		}
		return new IfTemplate(condition, trueBranch, falseBranch, tokenGot.getFileName(), tokenGot.getLineNumber());
	}

	private TemplateElement createWithTemplate(Token t) throws IOException,
			LexerException, ParserException {
		Token next = getTemplateLexer().expect1(TokenType.TT_IDENTIFIER);
		TemplateElement withVar = createExpression(next);
		next = getTemplateLexer().expect1r0(TokenType.TT_AS);
		String alias = null;
		if (next != null) {
			next = getTemplateLexer().expect1(TokenType.TT_ALIAS);
			alias = next.getValue();
		}
		next = getTemplateLexer().expect1(WITH_TEMPLATE_TYPES);
		TemplateElement template = next.extract();
		return new WithTemplate(withVar, alias, template, t.getFileName(), t.getLineNumber());
	}

	private TemplateElement createSetTemplate(Token t) throws IOException,
			LexerException, ParserException {
		Token next = getTemplateLexer().expect1(TokenType.TT_ALIAS);
		String alias = next.getValue();
		getTemplateLexer().expect1(TokenType.TT_TO);
		next = getTemplateLexer().expect1(TokenType.TT_IDENTIFIER);
		TemplateElement setVar = createExpression(next);
		return new VariableScopeTemplate(setVar, alias, t.getFileName(), t.getLineNumber());
	}

	public TemplateElement evaluate() {
		return null;
	}
}
