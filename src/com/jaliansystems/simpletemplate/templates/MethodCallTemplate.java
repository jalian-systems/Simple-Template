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

import com.jaliansystems.simpletemplate.Log;

public class MethodCallTemplate extends TemplateElement {

	private final String name;
	private final List<TemplateElement> paramValues;
	private MethodCallTemplate next;

	public MethodCallTemplate(String name, List<TemplateElement> paramValues,
			String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.name = name;
		this.paramValues = paramValues;
	}

	@Override
	public Object getTargetInternal(Scope scope) {
		Object o = scope.resolve(name);
		if (o == null) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name);
			return "";
		}
		if (!(o instanceof MethodDefinitionTemplate)) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name + " Found a " + o.getClass().getName() + " for " + name);
			return "";
		}
		MethodDefinitionTemplate st = (MethodDefinitionTemplate) o;
		List<String> paramNames = st.getParams();
		if (paramNames.size() != paramValues.size()) {
			Log.warning(getFileName(), getLineNumber(), "For subtemplate "
					+ name + " expecting " + paramNames.size()
					+ " parameters. Got: " + paramValues.size() + " parameters");
			return "";
		}
		Scope methodCallScope = new Scope();
		for (int i = 0; i < paramNames.size(); i++)
			methodCallScope.put(paramNames.get(i), paramValues.get(i).getTarget(scope));
		Object result = st.getTemplate().getTarget(methodCallScope);
		MethodCallTemplate nextTemplate = next ;
		while (nextTemplate != null) {
			nextTemplate.setVariable(new LiteralObjectTemplate(result, getFileName(), getLineNumber()));
			result = nextTemplate.getTarget(scope);
			nextTemplate = nextTemplate.getNext();
		}
		return result ;
	}

	@Override
	public String apply(Scope scope) {
		Object o = scope.resolve(name);
		if (o == null) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name);
			return "";
		}
		if (!(o instanceof MethodDefinitionTemplate)) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name + " Found a " + o.getClass().getName() + " for " + name);
			return "";
		}
		MethodDefinitionTemplate st = (MethodDefinitionTemplate) o;
		List<String> paramNames = st.getParams();
		if (paramNames.size() != paramValues.size()) {
			Log.warning(getFileName(), getLineNumber(), "For subtemplate "
					+ name + " expecting " + paramNames.size()
					+ " parameters. Got: " + paramValues.size() + " parameters");
			return "";
		}
		Scope methodCallScope = new Scope();
		for (int i = 0; i < paramNames.size(); i++)
			methodCallScope.put(paramNames.get(i), paramValues.get(i).getTarget(scope));
		String result = st.getTemplate().apply(methodCallScope);
		MethodCallTemplate nextTemplate = next ;
		while (nextTemplate != null) {
			nextTemplate.setVariable(new LiteralObjectTemplate(result, getFileName(), getLineNumber()));
			result = nextTemplate.apply(scope);
			nextTemplate = nextTemplate.getNext();
		}
		return result ;
	}

	private MethodCallTemplate getNext() {
		return next;
	}

	@Override
	public String getDisplayName(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(indent + "(call " + name + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (TemplateElement t : paramValues) {
			sb.append(t.getDisplayName("    " + indent)).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		if (next != null) {
			sb.append(indent + "  " + "(next-in-chain\n");
			sb.append(next.getDisplayName("    " + indent));
		}
		sb.append(indent + ")");
		return sb.toString();
	}

	@Override
	public String getDebugString(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(call " + name + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (TemplateElement t : paramValues) {
			sb.append(t.getDebugString("    " + indent)).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		if (next != null) {
			sb.append(indent + "  " + "(next-in-chain\n");
			sb.append(next.getDebugString("    " + indent));
		}
		sb.append(indent + ")");
		return sb.toString();
	}

	public void setVariable(TemplateElement vt) {
		paramValues.add(0, vt);
	}

	public void setNext(MethodCallTemplate next) {
		this.next = next;
	}

}
