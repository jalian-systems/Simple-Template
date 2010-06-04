package com.jaliansystems.simpletemplate.templates;


public class IfTemplate extends TemplateElement {

	private final TemplateElement condition;
	private final TemplateElement trueBranch;
	private final TemplateElement falseBranch;

	public IfTemplate(TemplateElement condition, TemplateElement trueBranch,
			TemplateElement falseBranch) {
		this.condition = condition;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	public TemplateElement getTarget(Scope scope) {
		if (condition.asBinary(scope))
			return trueBranch;
		return falseBranch;
	}

	@Override
	public String apply(Scope scope) {
		TemplateElement target = getTarget(scope);
		if (target != null)
			return target.apply(scope);
		return "" ;
	}
}
