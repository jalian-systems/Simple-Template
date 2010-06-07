package com.jaliansystems.simpletemplate.templates;

import static com.jaliansystems.simpletemplate.Log.warning;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LoopTemplate extends TemplateElement {

	private final TemplateElement template;
	private final TemplateElement loopVar;

	public LoopTemplate(TemplateElement loopVar, TemplateElement template, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.loopVar = loopVar;
		this.template = template;
	}

	@Override
	public String apply(Scope scope) {
		StringBuffer sb = new StringBuffer();
		Object target = loopVar.getTarget(scope);
		if (target instanceof Iterable<?>) {
			Iterator<?> iterator = ((Iterable<?>) target).iterator();
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
			warning(getFileName(), getLineNumber(), "You can loop only on Iterables, Arrays and Maps: got "
					+ target.getClass() + " for " + loopVar.getLispizedText(""));
		}
		return sb.toString();
	}

	@Override
	public Object getTarget(Scope scope) {
		Object r = null ;
		Object target = loopVar.getTarget(scope);
		if (target instanceof Iterable<?>) {
			Iterator<?> iterator = ((Iterable<?>) target).iterator();
			int index = 0;
			while (iterator.hasNext()) {
				Object o = iterator.next();
				Scope loopScope = new Scope(scope);
				loopScope.put("index0", index);
				loopScope.put("index1", index + 1);
				loopScope.put("it", o);
				r = template.getTarget(loopScope);
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
				r = template.getTarget(loopScope);
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
				r = template.getTarget(loopScope);
			}
		} else {
			warning(getFileName(), getLineNumber(), "You can loop only on Iterables, arrays and Maps: got "
					+ target.getClass() + " for " + loopVar.getLispizedText(""));
		}
		return r ;
	}

	@Override
	public String getLispizedText(String indent) {
		return getLineNumber() + ":" + indent + "(loop\n" + loopVar.getLispizedText("  " + indent) + "\n" + template.getLispizedText("  " + indent) + "\n" + indent + ")";
	}
}
