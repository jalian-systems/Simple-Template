package com.jaliansystems.simpletemplate.templates;

public class VariableScopeTemplate implements ITemplateElement {

	private final TemplateElement withVar;
	private final String alias;

	public VariableScopeTemplate(TemplateElement withVar, String alias) {
		this.withVar = withVar;
		this.alias = alias;
	}

	@Override
	public String apply(Scope scope) {
		scope.put(alias, withVar.getTarget(scope));
		return "" ;
	}

	@Override
	public boolean asBinary(Scope scope) {
		return false;
	}

}
