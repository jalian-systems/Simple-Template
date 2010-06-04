package com.jaliansystems.simpletemplate.templates;

import java.util.ArrayList;
import java.util.List;

public class CompositeTemplate extends TemplateElement {

	private List<TemplateElement> children = new ArrayList<TemplateElement>();
	
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

}
