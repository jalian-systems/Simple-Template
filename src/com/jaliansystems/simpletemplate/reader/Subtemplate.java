package com.jaliansystems.simpletemplate.reader;

import java.util.Iterator;
import java.util.List;

import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Subtemplate extends TemplateElement {

	private final String methodName;
	private final List<String> params;
	private final TemplateElement template ;
	
	public Subtemplate(String name, List<String> params, TemplateElement template, String fileName, int lineNumber) {
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
	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append("$");
		sb.append(methodName);
		sb.append("(");
		for (Iterator<String> iterator = params.iterator(); iterator.hasNext();) {
			String param = (String) iterator.next();
			sb.append(param);
			if (iterator.hasNext())
				sb.append(", ");
		}
		sb.append(")");
		sb.append(template.getName());
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
