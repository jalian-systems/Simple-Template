/*
 *   Copyright 2010 Jalian Systems Pvt. Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/

package com.jaliansystems.simpletemplate;

import java.util.HashMap;
import java.util.Map;

import com.jaliansystems.simpletemplate.internal.templates.AttributeEvaluator;

/**
 * A Scope object is used to pass variable replacements to a {@link Template#apply(Scope)} method.
 * 
 * <p>
 * Once a {@link Template} instance is created, you can apply a Scope and retrieve the resultant
 * result as a String.
 * </p>
*/

public class Scope {

	private Map<String, Object> scopedVariables = new HashMap<String, Object>();
	
	private final Object object;
	private final Scope parent;

	/**
	 * default constructor
	 */
	public Scope() {
		this(null, null);
	}

	/**
	 * Create a scope with the given map
	 * 
	 * @param variables a map of variable names and their values
	 */
	public Scope(Map<String, Object> variables) {
		this(null, null);
		scopedVariables = variables ;
	}
	
	/**
	 * @deprecated used internally
	 * @param parent
	 */
	public Scope(Scope parent) {
		this(parent, null);
	}

	/**
	 * @deprecated used internally
	 * 
	 * @param parent
	 * @param object
	 */
	public Scope(Scope parent, Object object) {
		this.parent = parent;
		this.object = object;
	}

	/**
	 * @deprecated used internally
	 * 
	 * @param key
	 * @return
	 */
	public Object resolve(String key) {
		if (key.startsWith("_") && parent != null)
			return parent.resolve(key);
		else if (key.startsWith("_")) {
			key = key.substring(1);
		}
		Object o = null ;
		if (object != null)
			try {
				o = new AttributeEvaluator(object, key).getValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			o = scopedVariables.get(key);
		if (o == null && parent != null)
			o = parent.resolve(key);
		return o;
	}

	/**
	 * Add a variable with the given name and value to the scope
	 * 
	 * @param name name of the variable
	 * @param value value of the variable
	 */
	public void put(String name, Object value) {
		scopedVariables.put(name, value);
	}
}
