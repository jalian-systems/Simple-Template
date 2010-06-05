package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import static com.jaliansystems.simpletemplate.Log.*;

public class IndexedAccessTemplate extends TemplateElement {

	private final TemplateElement variable;
	private final TemplateElement index;

	public IndexedAccessTemplate(TemplateElement variable,
			TemplateElement index) {
		this.variable = variable;
		this.index = index;
	}

	@Override
	public Object getTarget(Scope scope) {
		Object targetVariable = variable.getTarget(scope);
		Object targetIndex = index.getTarget(scope);
		if (targetVariable instanceof List<?>) {
			if (targetIndex instanceof Integer) {
				return ((List<?>) targetVariable).get(((Integer)targetIndex).intValue());
			} else {
				warning("TargetIndex expected to be an integer");
				return null ;
			}
		}
		
		if (targetVariable instanceof String) {
			if (targetIndex instanceof Integer) {
				return ((String) targetVariable).charAt(((Integer)targetIndex).intValue());
			} else {
				warning("TargetIndex expected to be an integer");
				return null ;
			}
		}
		
		if (targetVariable instanceof Map<?, ?>) {
			return ((Map<?,?>)targetVariable).get(targetIndex);
		}
		
		if (targetVariable != null && targetVariable.getClass().isArray()) {
			if (targetIndex instanceof Integer) {
				return Array.get(targetVariable, ((Integer)targetIndex).intValue());
			} else {
				warning("TargetIndex expected to be an integer");
				return null ;
			}
		}
		return null;
	}

}
