package com.jaliansystems.simpletemplate.templates;

import java.util.HashMap;

import static com.jaliansystems.simpletemplate.Log.*;

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
			o = new AttributeEvaluator(object, key).getValue();
		else
			o = get(key);
		if (o == null && parent != null)
			o = parent.get(key);
		if (o == null) {
			warning("Unable to resolve attribute " + key);
		}
		return o;
	}
}
