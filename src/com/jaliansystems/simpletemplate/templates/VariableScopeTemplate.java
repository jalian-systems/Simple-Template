package com.jaliansystems.simpletemplate.templates;

public class VariableScopeTemplate extends TemplateElement {

	private final TemplateElement setVar;
	private final String alias;

	public VariableScopeTemplate(TemplateElement withVar, String alias) {
		this.setVar = withVar;
		this.alias = alias;
	}

	@Override
	public String apply(Scope scope) {
		scope.put(alias, setVar.getTarget(scope));
		return "" ;
	}

	@Override
	public Object getTarget(Scope scope) {
		return setVar.getTarget(scope);
	}

	@Override
	public String getName() {
		return "$set " + alias + " to " + setVar.toString();
	}

}
