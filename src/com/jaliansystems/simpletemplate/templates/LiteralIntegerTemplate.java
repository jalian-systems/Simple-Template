package com.jaliansystems.simpletemplate.templates;


public class LiteralIntegerTemplate extends TemplateElement {

	private final int value;

	public LiteralIntegerTemplate(int value) {
		this.value = value;
	}
	
	@Override
	public Object getTarget(Scope scope) {
		return Integer.valueOf(value);
	}

}
