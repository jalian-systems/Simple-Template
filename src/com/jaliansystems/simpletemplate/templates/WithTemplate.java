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

public class WithTemplate extends TemplateElement {

	private final TemplateElement template;
	private final TemplateElement withVar;
	private final String alias;

	public WithTemplate(TemplateElement withVar, TemplateElement template, String fileName, int lineNumber) {
		this(withVar, null, template, fileName, lineNumber);
	}

	public WithTemplate(TemplateElement withVar, String alias, TemplateElement template, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.withVar = withVar;
		this.alias = alias;
		this.template = template;
	}

	public String apply(Scope scope) {
		Object target = withVar.getTarget(scope);
		Scope withScope;
		if (alias == null)
			withScope = new Scope(scope, target);
		else {
			withScope = new Scope(scope);
			withScope.put(alias, target);
		}
		return template.apply(withScope);
	}

	@Override
	public Object getTarget(Scope scope) {
		Object target = withVar.getTarget(scope);
		Scope withScope;
		if (alias == null)
			withScope = new Scope(scope, target);
		else {
			withScope = new Scope(scope);
			withScope.put(alias, target);
		}
		return template.getTarget(withScope);
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "(with" + 
				(alias == null ? "" : " (alias " + alias + ")") + "\n" + 
				withVar.getDisplayName("  " + indent) + "\n" +
				template.getDisplayName("  " + indent) +
				(indent + ")");
				
	}

	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "(with" + 
				(alias == null ? "" : " (alias " + alias + ")") + "\n" + 
				withVar.getDebugString("  " + indent) + 
				template.getDebugString("  " + indent) +
				(indent + ")");
				
	}
}
