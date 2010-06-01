package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Scope extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private final Object object;
	private final Scope parent;

	public Scope() {
		this(null, null);
	}

	public Scope(Scope parent) {
		this(parent, null);
	}

	public Scope(Scope parent, Object object) {
		this.parent = parent;
		this.object = object;
	}

	public Object resolve(String key) {
		if (key.startsWith("_") && parent != null)
			return parent.resolve(key);
		else if (key.startsWith("_")) {
			key = key.substring(1);
		}
		Object o = null ;
		if (object != null)
			o = getAttributeByReflection(object, key);
		else
			o = get(key);
		if (o == null && parent != null)
			o = parent.get(key);
		if (o == null) {
			Log.warning("Unable to resolve attribute " + key);
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

}
