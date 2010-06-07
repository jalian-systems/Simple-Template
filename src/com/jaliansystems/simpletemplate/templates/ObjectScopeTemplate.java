package com.jaliansystems.simpletemplate.templates;

public class ObjectScopeTemplate extends TemplateElement {

	private final TemplateElement object;
	private VariableTemplate variable;

	public ObjectScopeTemplate(TemplateElement object, String variable, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.object = object;
		this.variable = new VariableTemplate(variable, fileName, lineNumber);
	}
	
	@Override
	public Object getTarget(Scope scope) {
		Object oTarget = object.getTarget(scope);
		Scope objectScope = new Scope(null, oTarget);
		return variable.getTarget(objectScope);
	}

	@Override
	public String getLispizedText(String indent) {
		return indent + "(attribute-of\n" +
							object.getLispizedText("  " + indent) + "\n" +
							variable.getLispizedText("  " + indent) + "\n" + ")" ;
	}

}
