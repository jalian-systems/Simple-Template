package com.jaliansystems.simpletemplate.templates;

public class WithTemplate extends TemplateElement {

	private final TemplateElement template;
	private final TemplateElement withVar;
	private final String alias;

	public WithTemplate(TemplateElement withVar, TemplateElement template, String fileName, int lineNumber) {
		this(withVar, null, template, fileName, lineNumber);
	}

	public WithTemplate(TemplateElement withVar, String alias, TemplateElement template, String fileName, int lineNumber) {
		super(fileName, lineNumber);
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
	public Object getTarget(Scope scope) {
		Object target = withVar.getTarget(scope);
		Scope withScope;
		if (alias == null)
			withScope = new Scope(scope, target);
		else {
			withScope = new Scope(scope);
			withScope.put(alias, target);
		}
		return template.getTarget(withScope);
	}

	@Override
	public String getLispizedText(String indent) {
		return getLineNumber() + ":" + indent + "(with" + 
				(alias == null ? "" : " (alias " + alias + ")") + "\n" + 
				withVar.getLispizedText("  " + indent) + 
				template.getLispizedText("  " + indent) +
				(indent + ")");
				
	}
}
