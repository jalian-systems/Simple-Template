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


public class LiteralTextTemplate extends TemplateElement {

	private final String text;

	public LiteralTextTemplate(String text, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.text = text;
	}
	
	@Override
	public Object getTargetInternal(Scope scope) {
		return text;
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "\"" + text.replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n") + "\"";
	}
	
	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "\"" + text.replaceAll("\"", "\\\\\"").replaceAll("\n", "\\\\n") + "\"";
	}
	
	@Override
	public String apply(Scope scope) {
		return text ;
	}
}
