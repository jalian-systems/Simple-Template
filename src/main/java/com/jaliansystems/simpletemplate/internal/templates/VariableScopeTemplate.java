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

public class VariableScopeTemplate extends TemplateElement {

	private final TemplateElement setVar;
	private final String alias;

	public VariableScopeTemplate(TemplateElement withVar, String alias, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.setVar = withVar;
		this.alias = alias;
	}

	@Override
	public String apply(Scope scope) {
		scope.put(alias, setVar.getTarget(scope));
		return "" ;
	}

	@Override
	public Object getTargetInternal(Scope scope) {
		return setVar.getTarget(scope);
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "(set\n" +
				setVar.getDisplayName(indent + "  ") + "\n" +
				(indent + "  " + alias) + "\n" +
				(indent + ")") ;
	}

	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "(set\n" +
				setVar.getDebugString(indent + "  ") + "\n" +
				(indent + "  " + alias) + "\n" +
				(indent + ")") ;
	}

}
