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

import java.lang.reflect.Method;

public class AttributeEvaluator {

	private final String attr;
	private final Object o;

	public AttributeEvaluator(Object o, String attr) {
		this.o = o;
		this.attr = attr;
	}
	
	public Object getValue() throws Exception {
		Object attribute = null;
		String[] prefixes = new String[] { "get", "is", "" } ;
		for (int i = 0; i < prefixes.length; i++) {
			try {
				attribute = getAttribute(o, attr, prefixes[i]);
				break ;
			} catch (Exception e) {
				if (i == prefixes.length - 1)
					throw e;
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
