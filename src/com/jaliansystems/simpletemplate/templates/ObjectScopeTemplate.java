package com.jaliansystems.simpletemplate.templates;

public class ObjectScopeTemplate extends TemplateElement {

	private final TemplateElement object;
	private VariableTemplate variable;

	public ObjectScopeTemplate(TemplateElement object, String variable) {
		this.object = object;
		this.variable = new VariableTemplate(variable);
	}
	
	@Override
	public Object getTarget(Scope scope) {
		Object oTarget = object.getTarget(scope);
		Scope objectScope = new Scope(null, oTarget);
		return variable.getTarget(objectScope);
	}

	@Override
	public String getName() {
		return object.toString() + "." + variable;
	}

}
