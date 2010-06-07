package com.jaliansystems.simpletemplate.templates;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.jaliansystems.simpletemplate.Log.*;

public class VariableTemplate extends TemplateElement {

	private final String variable;

	public VariableTemplate(String variable, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.variable = variable;
	}

	public Object getTarget(Scope scope) {
		String[] tokens = getTokens(variable);
		if (tokens.length == 0)
			return null;
		Object o = null;
		try {
			o = scope.resolve(tokens[0]);
		} catch (Exception e) {
			warning(getFileName(), getLineNumber(), "Unable to resolve attribute " + variable + " for attribute part " + tokens[0]);
		}
		if (o == null)
			return null;
		for (int i = 1; i < tokens.length; i++) {
			try {
				o = new AttributeEvaluator(o, tokens[i]).getValue();
			} catch (Exception e) {
				warning(getFileName(), getLineNumber(), "Unable to resolve attribute " + variable + " for attribute part " + tokens[i]);
				o = null ;
			}
			if (o == null) {
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
	public String getLispizedText(String indent) {
		return indent + variable;
	}
}
