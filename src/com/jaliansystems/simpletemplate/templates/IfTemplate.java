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

public class IfTemplate extends TemplateElement {

	private final TemplateElement condition;
	private final TemplateElement trueBranch;
	private final TemplateElement falseBranch;

	public IfTemplate(TemplateElement condition, TemplateElement trueBranch,
			TemplateElement falseBranch, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.condition = condition;
		this.trueBranch = trueBranch;
		this.falseBranch = falseBranch;
	}

	public TemplateElement getTarget(Scope scope) {
		if (condition.asBinary(scope))
			return trueBranch;
		return falseBranch;
	}

	@Override
	public String apply(Scope scope) {
		TemplateElement target = getTarget(scope);
		if (target != null)
			return target.apply(scope);
		return "";
	}

	@Override
	public String getDisplayName(String indent) {
		if (falseBranch != null) {
			return getLineNumber() + ":" + indent + "(ifelse\n" + condition.getDisplayName("  " + indent)
					+ "\n" + trueBranch.getDisplayName("  " + indent) + "\n"
					+ falseBranch.getDisplayName("  " + indent) + "\n" + indent + ")";
		} else {
			return getLineNumber() + ":" + indent + "(if\n" + condition.getDisplayName("  " + indent)
					+ "\n" + trueBranch.getDisplayName("  " + indent) + "\n"
					+ indent + ")";
		}
	}
}
