package com.jaliansystems.simpletemplate.templates;


public class LiteralBooleanTemplate extends TemplateElement {

	private final boolean value;

	public LiteralBooleanTemplate(boolean value, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.value = value;
	}
	
	@Override
	public Object getTarget(Scope scope) {
		return value;
	}

	@Override
	public String getLispizedText(String indent) {
		return indent + Boolean.toString(value);
	}

}
