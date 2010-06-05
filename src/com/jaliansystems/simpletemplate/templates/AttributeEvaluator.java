package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Method;

public class AttributeEvaluator {

	private final String attr;
	private final Object o;

	public AttributeEvaluator(Object o, String attr) {
		this.o = o;
		this.attr = attr;
	}
	
	public Object getValue() {
		Object attribute = null;
		String[] prefixes = new String[] { "get", "is", "" } ;
		for (int i = 0; i < prefixes.length; i++) {
			try {
				attribute = getAttribute(o, attr, prefixes[i]);
			} catch (Exception e) {
			}
		}
		return attribute;
	}

	private Object getAttribute(Object o, String attr, String prefix) throws Exception {
		Method method = o.getClass().getMethod(prefix + (prefix.isEmpty() ? attr : camelCase(attr)),
				new Class[] {});
		return method.invoke(o, new Object[] {});
	}

	private String camelCase(String attr) {
		return Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
	}

}
