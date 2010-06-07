package com.jaliansystems.simpletemplate.templates;

import java.util.List;


public class MethodDefinitionTemplate extends TemplateElement {

	private final String methodName;
	private final List<String> params;
	private final TemplateElement template ;
	
	public MethodDefinitionTemplate(String name, List<String> params, TemplateElement template, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.methodName = name;
		this.params = params;
		this.template = template;
	}

	@Override
	public Object getTarget(Scope scope) {
		return null;
	}

	@Override
	public String getLispizedText(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(indent + "(method-def " + methodName + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (String t : params) {
			sb.append("    " + indent + t).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		sb.append(template.getLispizedText("  " + indent));
		sb.append(indent + ")");
		return sb.toString();
	}

	@Override
	public String apply(Scope scope) {
		scope.put(methodName, this);
		return "";
	}

	public String getMethodName() {
		return methodName;
	}
	
	public List<String> getParams() {
		return params;
	}

	public TemplateElement getTemplate() {
		return template;
	}
	
}
