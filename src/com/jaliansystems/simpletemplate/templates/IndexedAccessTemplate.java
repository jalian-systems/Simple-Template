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
import java.util.List;
import java.util.Map;

import static com.jaliansystems.simpletemplate.Log.*;

public class IndexedAccessTemplate extends TemplateElement {

	private final TemplateElement variable;
	private final TemplateElement index;

	public IndexedAccessTemplate(TemplateElement variable,
			TemplateElement index, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.variable = variable;
		this.index = index;
	}

	@Override
	public Object getTarget(Scope scope) {
		Object targetVariable = variable.getTarget(scope);
		Object targetIndex = index.getTarget(scope);
		if (targetVariable == null)
			return null ;
		if (targetIndex == null) {
			warning(getFileName(), getLineNumber(), "Can't index with a null value " + index.getDisplayName("") + " in " + variable.getDisplayName(""));
			return null ;
		}
		if (targetVariable instanceof List<?>) {
			if (targetIndex instanceof Integer) {
				return ((List<?>) targetVariable).get(((Integer)targetIndex).intValue());
			} else {
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getDisplayName("") + " in " + variable.getDisplayName(""));
				return null ;
			}
		}
		
		if (targetVariable instanceof String) {
			if (targetIndex instanceof Integer) {
				return ((String) targetVariable).charAt(((Integer)targetIndex).intValue());
			} else {
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getDisplayName("") + " in " + variable.getDisplayName(""));
				return null ;
			}
		}
		
		if (targetVariable instanceof Map<?, ?>) {
			return ((Map<?,?>)targetVariable).get(targetIndex);
		}
		
		if (targetVariable != null && targetVariable.getClass().isArray()) {
			if (targetIndex instanceof Integer) {
				return Array.get(targetVariable, ((Integer)targetIndex).intValue());
			} else {
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getDisplayName("") + " in " + variable.getDisplayName(""));
				return null ;
			}
		}
		warning(getFileName(), getLineNumber(), "Can't index into " + variable.getDisplayName("") + " Class: " + targetVariable.getClass().getName());
		return null ;
	}

	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "(indexed-access\n"
						+ variable.getDebugString("  " + indent) + "\n" + index.getDebugString("  " + indent) + "\n" + indent + ")";
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "(indexed-access\n"
						+ variable.getDisplayName("  " + indent) + "\n" + index.getDisplayName("  " + indent) + "\n" + indent + ")";
	}
}
