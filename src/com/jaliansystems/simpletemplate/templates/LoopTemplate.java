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

import static com.jaliansystems.simpletemplate.Log.warning;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LoopTemplate extends TemplateElement {

	private static interface Collector {
		public void collect(TemplateElement t, Scope scope);
	}

	private static class TargetCollector implements Collector {
		private Object r;

		public void collect(TemplateElement t, Scope scope) {
			r = t.getTarget(scope);
		}

		public Object getValue() {
			return r;
		}

	}

	private final TemplateElement template;
	private final TemplateElement loopVar;

	public LoopTemplate(TemplateElement loopVar, TemplateElement template,
			String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.loopVar = loopVar;
		this.template = template;
	}

	@Override
	public String apply(Scope scope) {
		final StringBuffer sb = new StringBuffer();
		runLoop(scope, new Collector() {
			public void collect(TemplateElement t, Scope scope) {
				sb.append(t.apply(scope));
			}
		});
		return sb.toString();
	}

	@Override
	public Object getTargetInternal(Scope scope) {
		TargetCollector tc = new TargetCollector();
		runLoop(scope, tc);
		return tc.getValue();
	}

	private void runLoop(Scope scope, Collector collector) {
		Object target = loopVar.getTarget(scope);
		if (target == null) {
			warning(getFileName(), getLineNumber(),
					"You can loop only on Iterables, arrays and Maps: got null target for "
							+ loopVar.getDisplayName(""));
			return;
		}
		if (target != null && target.getClass().isArray())
			target = Arrays.asList((Object[]) target);
		if (target instanceof Iterable<?>) {
			Iterator<?> iterator = ((Iterable<?>) target).iterator();
			int index = 0;
			while (iterator.hasNext()) {
				Object o = iterator.next();
				Scope loopScope = new Scope(scope);
				loopScope.put("index0", index);
				loopScope.put("index1", index + 1);
				loopScope.put("it", o);
				collector.collect(template, loopScope);
				index++;
			}
		} else if (target instanceof Map<?, ?>) {
			Iterator<?> iterator = ((Map<?, ?>) target).entrySet().iterator();
			int index = 0;
			while (iterator.hasNext()) {
				Entry<?, ?> next = (Entry<?, ?>) iterator.next();
				Scope loopScope = new Scope(scope);
				loopScope.put("index0", index);
				loopScope.put("index1", index + 1);
				loopScope.put("key", next.getKey());
				loopScope.put("value", next.getValue());
				collector.collect(template, loopScope);
				index++;
			}
		} else {
			warning(getFileName(),
					getLineNumber(),
					"You can loop only on Iterables, Arrays and Maps: got "
							+ target.getClass() + " for "
							+ loopVar.getDisplayName(""));
		}
	}

	@Override
	public String getDisplayName(String indent) {
		return indent + "(loop\n" + loopVar.getDisplayName("  " + indent)
				+ "\n" + template.getDisplayName("  " + indent) + "\n" + indent
				+ ")";
	}

	@Override
	public String getDebugString(String indent) {
		return getLineNumber() + ":" + indent + "(loop\n"
				+ loopVar.getDebugString("  " + indent) + "\n"
				+ template.getDebugString("  " + indent) + "\n" + indent + ")";
	}
}
