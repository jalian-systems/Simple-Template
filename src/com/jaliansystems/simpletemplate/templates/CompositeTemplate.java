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
import java.util.List;

public class CompositeTemplate extends TemplateElement {

	private List<TemplateElement> children = new ArrayList<TemplateElement>();
	
	public CompositeTemplate(String fileName, int lineNumber) {
		super(fileName, lineNumber);
	}

	public void add(TemplateElement t) {
		children.add(t);
	}

	@Override
	public String apply(Scope scope) {
		StringBuffer buffer = new StringBuffer();
		for (TemplateElement t : children) {
			buffer.append(t.apply(scope));
		}
		return buffer.toString();
	}

	@Override
	public Object getTarget(Scope scope) {
		Object o = null ;
		for (TemplateElement t : children)
			o = t.getTarget(scope);
		return o;
	}

	@Override
	public String getDisplayName(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(composite\n");
		for (TemplateElement t : children) {
			sb.append(t.getDisplayName(indent + "  ")).append('\n');
		}
		sb.append(indent + ")");
		return sb.toString() ;
	}

	@Override
	public String getDebugString(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(composite\n");
		for (TemplateElement t : children) {
			sb.append(t.getDebugString(indent + "  ")).append('\n');
		}
		sb.append(indent + ")");
		return sb.toString() ;
	}
}
