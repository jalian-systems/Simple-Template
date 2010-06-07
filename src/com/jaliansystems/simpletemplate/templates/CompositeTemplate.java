package com.jaliansystems.simpletemplate.templates;

import java.util.ArrayList;
import java.util.List;

public class CompositeTemplate extends TemplateElement {

	private List<TemplateElement> children = new ArrayList<TemplateElement>();
	
	public CompositeTemplate(String fileName, int lineNumber) {
		super(fileName, lineNumber);
	}

	public void add(TemplateElement t) {
		children.add(t);
	}

	@Override
	public String apply(Scope scope) {
		StringBuffer buffer = new StringBuffer();
		for (TemplateElement t : children) {
			buffer.append(t.apply(scope));
		}
		return buffer.toString();
	}

	@Override
	public Object getTarget(Scope scope) {
		Object o = null ;
		for (TemplateElement t : children)
			o = t.getTarget(scope);
		return o;
	}

	@Override
	public String getLispizedText(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(composite\n");
		for (TemplateElement t : children) {
			sb.append(t.getLispizedText(indent + "  ")).append('\n');
		}
		sb.append(indent + ")");
		return sb.toString() ;
	}
}
