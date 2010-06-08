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

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.jaliansystems.simpletemplate.Log.*;

public class VariableTemplate extends TemplateElement {

	private final String variable;

	public VariableTemplate(String variable, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.variable = variable;
	}

	public Object getTarget(Scope scope) {
		String[] tokens = getTokens(variable);
		if (tokens.length == 0)
			return null;
		Object o = null;
		try {
			o = scope.resolve(tokens[0]);
		} catch (Exception e) {
			warning(getFileName(), getLineNumber(), "Unable to resolve attribute " + variable + " for attribute part " + tokens[0]);
		}
		if (o == null)
			return null;
		for (int i = 1; i < tokens.length; i++) {
			try {
				o = new AttributeEvaluator(o, tokens[i]).getValue();
			} catch (Exception e) {
				warning(getFileName(), getLineNumber(), "Unable to resolve attribute " + variable + " for attribute part " + tokens[i]);
				o = null ;
			}
			if (o == null) {
				break;
			}
		}
		return o;
	}

	private String[] getTokens(String fqva) {
		ArrayList<String> elements = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(fqva, ".");
		while (tok.hasMoreTokens()) {
			elements.add(tok.nextToken());
		}
		return elements.toArray(new String[elements.size()]);
	}

	@Override
	public String getLispizedText(String indent) {
		return getLineNumber() + ":" + indent + variable;
	}
}
