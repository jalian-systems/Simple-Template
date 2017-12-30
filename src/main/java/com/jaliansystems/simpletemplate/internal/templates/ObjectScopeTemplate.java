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

package com.jaliansystems.simpletemplate.internal.templates;

import com.jaliansystems.simpletemplate.Scope;

public class ObjectScopeTemplate extends TemplateElement {

	private final TemplateElement object;
	private VariableTemplate variable;

	public ObjectScopeTemplate(TemplateElement object, String variable, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.object = object;
		this.variable = new VariableTemplate(variable, fileName, lineNumber);
	}
	
	@Override
	public Object getTargetInternal(Scope scope) {
		Object oTarget = object.getTarget(scope);
		Scope objectScope = new Scope(null, oTarget);
		return variable.getTarget(objectScope);
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "(attribute-of\n" +
							object.getDisplayName("  " + indent) + "\n" +
							variable.getDisplayName("  " + indent) + "\n" + ")" ;
	}

	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "(attribute-of\n" +
							object.getDebugString("  " + indent) + "\n" +
							variable.getDebugString("  " + indent) + "\n" + ")" ;
	}

}
