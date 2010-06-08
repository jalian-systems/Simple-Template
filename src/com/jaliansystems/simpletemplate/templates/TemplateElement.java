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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public abstract class TemplateElement {
	public abstract Object getTarget(Scope scope);

	private String fileName ;
	private int lineNumber ;
	
	public TemplateElement(String fileName, int lineNumber) {
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}
	
	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.TemplateElement#apply(java.util.Map)
	 */
	public String apply(Scope scope) {
		Object object = getTarget(scope);
		if (object == null) {
			return "";
		}
		return object.toString();
	}

	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.TemplateElement#asBinary(java.util.Map)
	 */
	public final boolean asBinary(Scope scope) {
		Object o = getTarget(scope);
		if (o == null)
			return false;
		if (o instanceof Boolean)
			return ((Boolean)o).booleanValue();
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
			return Array.getLength(o) > 0 ;
		}
		if (o instanceof String) {
			return ((String)o).length() > 0 ;
		}
		return true;
	}
	
	public abstract String getDisplayName(String indent);

	public abstract String getDebugString(String indent);

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
