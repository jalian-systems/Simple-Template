package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LoopTemplate implements ITemplateElement {

	private final ITemplateElement template;
	private final TemplateElement loopVar;

	public LoopTemplate(TemplateElement loopVar, ITemplateElement template) {
		this.loopVar = loopVar;
		this.template = template;
	}

	@Override
	public String apply(Scope scope) {
		StringBuffer sb = new StringBuffer();
		Object target = loopVar.getTarget(scope);
		if (target instanceof Collection<?>) {
			Iterator<?> iterator = ((Collection<?>) target).iterator();
			int index = 0;
			while (iterator.hasNext()) {
				Object o = iterator.next();
				Scope loopScope = new Scope(scope);
				loopScope.put("index0", index);
				loopScope.put("index1", index + 1);
				loopScope.put("it", o);
				sb.append(template.apply(loopScope));
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
				sb.append(template.apply(loopScope));
				index++;
			}
		} else if (target != null && target.getClass().isArray()) {
			int length = Array.getLength(target);
			for (int index = 0; index < length; index++) {
				Object o = Array.get(target, index);
				Scope loopScope = new Scope(scope);
				loopScope.put("index0", index);
				loopScope.put("index1", index + 1);
				loopScope.put("it", o);
				sb.append(template.apply(loopScope));
			}
		} else {
			Log.warning("You can loop only on Collections and Maps: got "
					+ target.getClass() + " for " + loopVar);
		}
		return sb.toString();
	}

	@Override
	public boolean asBinary(Scope scope) {
		return false;
	}

}
