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

import java.util.List;


public class MethodDefinitionTemplate extends TemplateElement {

	private final String methodName;
	private final List<String> params;
	private final TemplateElement template ;
	
	public MethodDefinitionTemplate(String name, List<String> params, TemplateElement template, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.methodName = name;
		this.params = params;
		this.template = template;
	}

	@Override
	public Object getTarget(Scope scope) {
		return null;
	}

	@Override
	public String getDisplayName(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(method-def " + methodName + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (String t : params) {
			sb.append("    " + indent + t).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		sb.append(template.getDisplayName("  " + indent));
		sb.append(indent + ")");
		return sb.toString();
	}

	@Override
	public String getDebugString(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(method-def " + methodName + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (String t : params) {
			sb.append("    " + indent + t).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		sb.append(template.getDebugString("  " + indent));
		sb.append(indent + ")");
		return sb.toString();
	}

	@Override
	public String apply(Scope scope) {
		scope.put(methodName, this);
		return "";
	}

	public String getMethodName() {
		return methodName;
	}
	
	public List<String> getParams() {
		return params;
	}

	public TemplateElement getTemplate() {
		return template;
	}
	
}
