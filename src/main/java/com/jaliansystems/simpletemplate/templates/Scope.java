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

package com.jaliansystems.simpletemplate.templates;

import java.util.HashMap;

public class Scope {

	private HashMap<String, Object> scopedVariables = new HashMap<String, Object>();
	
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

	public void put(String name, Object value) {
		scopedVariables.put(name, value);
	}
}
