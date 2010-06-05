package com.jaliansystems.simpletemplate.templates;


public class LiteralTextTemplate extends TemplateElement {

	private final String text;

	public LiteralTextTemplate(String text) {
		this.text = text;
	}
	
	@Override
	public Object getTarget(Scope scope) {
		return text;
	}

	@Override
	public String getName() {
		return "\"" + text.replaceAll("\"", "\\\\\"") + "\"";
	}
}
