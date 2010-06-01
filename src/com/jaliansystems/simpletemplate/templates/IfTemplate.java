package com.jaliansystems.simpletemplate.templates;


public class IfTemplate implements ITemplateElement {

	private final ITemplateElement condition;
	private final ITemplateElement trueBranch;
	private final ITemplateElement falseBranch;

	public IfTemplate(ITemplateElement condition, ITemplateElement trueBranch,
			ITemplateElement falseBranch) {
		this.condition = condition;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	public ITemplateElement getTarget(Scope scope) {
		if (condition.asBinary(scope))
			return trueBranch;
		return falseBranch;
	}

	@Override
	public String apply(Scope scope) {
		ITemplateElement target = getTarget(scope);
		if (target != null)
			return target.apply(scope);
		return "" ;
	}

	@Override
	public boolean asBinary(Scope scope) {
		return getTarget(scope).asBinary(scope);
	}

}
