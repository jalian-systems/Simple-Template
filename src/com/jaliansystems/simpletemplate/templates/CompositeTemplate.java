package com.jaliansystems.simpletemplate.templates;

import java.util.ArrayList;
import java.util.List;

public class CompositeTemplate implements ITemplateElement {

	private List<ITemplateElement> children = new ArrayList<ITemplateElement>();
	
	public void add(ITemplateElement t) {
		children.add(t);
	}

	@Override
	public String apply(Scope scope) {
		StringBuffer buffer = new StringBuffer();
		for (ITemplateElement t : children) {
			buffer.append(t.apply(scope));
		}
		return buffer.toString();
	}

	@Override
	public boolean asBinary(Scope scope) {
		return false;
	}

}
