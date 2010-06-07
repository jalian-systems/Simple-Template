package com.jaliansystems.simpletemplate.templates;

public class IfTemplate extends TemplateElement {

	private final TemplateElement condition;
	private final TemplateElement trueBranch;
	private final TemplateElement falseBranch;

	public IfTemplate(TemplateElement condition, TemplateElement trueBranch,
			TemplateElement falseBranch, String fileName, int lineNumber) {
		super(fileName, lineNumber);
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
		return "";
	}

	@Override
	public String getLispizedText(String indent) {
		if (falseBranch != null) {
			return indent + "(ifelse\n" + condition.getLispizedText("  " + indent)
					+ "\n" + trueBranch.getLispizedText("  " + indent) + "\n"
					+ falseBranch.getLispizedText("  " + indent) + "\n" + indent + ")";
		} else {
			return indent + "(if\n" + condition.getLispizedText("  " + indent)
					+ "\n" + trueBranch.getLispizedText("  " + indent) + "\n"
					+ indent + ")";
		}
	}
}
