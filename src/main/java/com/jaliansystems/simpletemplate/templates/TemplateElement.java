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

import static com.jaliansystems.simpletemplate.Log.warning;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public abstract class TemplateElement {
	private String fileName;
	private int lineNumber;

	public TemplateElement(String fileName, int lineNumber) {
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}

	public abstract Object getTargetInternal(Scope scope);

	public Object getTarget(Scope scope) {
		try {
			return getTargetInternal(scope);
		} catch (Throwable t) {
			warning(getFileName(), getLineNumber(), "Unexpected exception "
					+ t.getClass().getName() + ": " + t.getMessage()
					+ " while processing " + getDisplayName(""));
			return null;
		}
	}

	public String apply(Scope scope) {
		return toString(getTarget(scope), scope);
	}

	private String toString(Object o, Scope scope) {
		if (o == null)
			return "";
		if (o instanceof Collection<?>) {
			return listToString(((Collection<?>) o), scope);
		}
		if (o.getClass().isArray()) {
			return listToString(Arrays.asList((Object[]) o), scope);
		}
		return o.toString();
	}

	private String listToString(Collection<?> c, Scope scope) {
		String listSep = getValue(scope, "st_list_separator", ",");
		String listPrefix = getValue(scope, "st_list_prefix", "");
		String listSuffix = getValue(scope, "st_list_suffix", "");
		StringBuffer sb = new StringBuffer();
		Object[] a = c.toArray();
		for (int i = 0; i < a.length; i++) {
			sb.append(listPrefix);
			sb.append(a[i].toString());
			sb.append(listSuffix);
			if (i != a.length - 1)
				sb.append(listSep);
		}
		return sb.toString();
	}

	private String getValue(Scope scope, String key, String defaultValue) {
		String listSep = defaultValue;
		Object o = scope.resolve(key);
		if (o instanceof String) {
			listSep = (String) o;
		}
		return listSep;
	}

	public final boolean asBinary(Scope scope) {
		Object o = getTarget(scope);
		if (o == null)
			return false;
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue();
		if (o instanceof Number) {
			return !o.equals(Integer.valueOf(0));
		}
		if (o instanceof Collection<?>) {
			return ((Collection<?>) o).size() != 0;
		}
		if (o instanceof Map<?, ?>) {
			return ((Map<?, ?>) o).size() != 0;
		}
		if (o.getClass().isArray()) {
			return Array.getLength(o) > 0;
		}
		if (o instanceof String) {
			return ((String) o).length() > 0;
		}
		return true;
	}

	public abstract String getDisplayName(String indent);

	public abstract String getDebugString(String indent);

	@Override
	public String toString() {
		return getDisplayName("");
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
