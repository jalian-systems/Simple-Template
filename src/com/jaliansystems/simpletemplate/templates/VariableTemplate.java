package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
			o = getAttributeByReflection(o, tokens[i]);
			if (o == null)
				break;
		}
		return o;
	}

	private Object getAttributeByReflection(Object o, String attr) {
		Object attribute = null;
		try {
			attribute = getAttribute(o, attr, "get");
		} catch (NoSuchMethodException e) {
			try {
				attribute = getAttribute(o, attr, "is");
			} catch (NoSuchMethodException e1) {
				Log.warning("Unable to find the attribute " + attr + " in class " + o.getClass().getName());
			} catch (Exception e2) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attribute;
	}

	private Object getAttribute(Object o, String attr, String prefix) throws Exception {
		Method method = o.getClass().getMethod(prefix + camelCase(attr),
				new Class[] {});
		return method.invoke(o, new Object[] {});
	}

	private String camelCase(String attr) {
		return Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
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
