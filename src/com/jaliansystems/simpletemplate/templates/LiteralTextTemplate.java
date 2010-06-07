package com.jaliansystems.simpletemplate.templates;


public class LiteralTextTemplate extends TemplateElement {

	private final String text;

	public LiteralTextTemplate(String text, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.text = text;
	}
	
	@Override
	public Object getTarget(Scope scope) {
		return text;
	}

	@Override
	public String getLispizedText(String indent) {
		return getLineNumber() + ":" + indent + "\"" + text.replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n") + "\"";
	}
	
	@Override
	public String apply(Scope scope) {
		return text ;
	}
}
