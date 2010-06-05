package com.jaliansystems.simpletemplate.templates;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.jaliansystems.simpletemplate.Log.*;

public class VariableTemplate extends TemplateElement {

	private final String variable;

	public VariableTemplate(String variable) {
		this.variable = variable;
	}

	public Object getTarget(Scope scope) {
		String[] tokens = getTokens(variable);
		if (tokens.length == 0)
			return null;
		Object o = scope.resolve(tokens[0]);
		if (o == null)
			return null;
		for (int i = 1; i < tokens.length; i++) {
			o = new AttributeEvaluator(o, tokens[i]).getValue();
			if (o == null) {
				warning("Unable to resolve attribute " + variable + " for attribute part " + tokens[i]);
				break;
			}
		}
		return o;
	}

	private String[] getTokens(String fqva) {
		ArrayList<String> elements = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(fqva, ".");
		while (tok.hasMoreTokens()) {
			elements.add(tok.nextToken());
		}
		return elements.toArray(new String[elements.size()]);
	}

	@Override
	public String toString() {
		return variable ;
	}
}
