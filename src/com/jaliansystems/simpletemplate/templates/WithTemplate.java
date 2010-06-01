package com.jaliansystems.simpletemplate.templates;

public class WithTemplate implements ITemplateElement {

	private final ITemplateElement template;
	private final TemplateElement withVar;
	private final String alias;

	public WithTemplate(TemplateElement withVar, ITemplateElement template) {
		this(withVar, null, template);
	}

	public WithTemplate(TemplateElement withVar, String alias, ITemplateElement template) {
		this.withVar = withVar;
		this.alias = alias;
		this.template = template;
	}

	public String apply(Scope scope) {
		Object target = withVar.getTarget(scope);
		Scope withScope;
		if (alias == null)
			withScope = new Scope(scope, target);
		else {
			withScope = new Scope(scope);
			withScope.put(alias, target);
		}
		return template.apply(withScope);
	}

	@Override
	public boolean asBinary(Scope scope) {
		return false;
	}

}
