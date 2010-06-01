package com.jaliansystems.simpletemplate.templates;

import java.util.Collection;
import java.util.Map;

public abstract class TemplateElement implements ITemplateElement {
	protected abstract Object getTarget(Scope scope);

	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.ITemplateElement#apply(java.util.Map)
	 */
	@Override
	public String apply(Scope scope) {
		Object object = getTarget(scope);
		if (object == null) {
			return "";
		}
		return object.toString();
	}

	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.ITemplateElement#asBinary(java.util.Map)
	 */
	@Override
	public boolean asBinary(Scope scope) {
		Object o = getTarget(scope);
		if (o == null)
			return false;
		if (o instanceof Boolean)
			return ((Boolean)o).booleanValue();
		if (o instanceof Number) {
			return !o.equals(Integer.valueOf(0));
		}
		if (o instanceof Collection<?>) {
			return ((Collection<?>) o).size() != 0;
		}
		if (o instanceof Map<?, ?>) {
			return ((Map<?, ?>) o).size() != 0;
		}
		return true;
	}
}
