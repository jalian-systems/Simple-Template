package com.jaliansystems.simpletemplate.templates;


public class LiteralIntegerTemplate extends TemplateElement {

	private final int value;

	public LiteralIntegerTemplate(int value, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.value = value;
	}
	
	@Override
	public Object getTarget(Scope scope) {
		return Integer.valueOf(value);
	}

	@Override
	public String getLispizedText(String indent) {
		return indent + Integer.toString(value);
	}

}
