package com.jaliansystems.simpletemplate.templates;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import static com.jaliansystems.simpletemplate.Log.*;

public class IndexedAccessTemplate extends TemplateElement {

	private final TemplateElement variable;
	private final TemplateElement index;

	public IndexedAccessTemplate(TemplateElement variable,
			TemplateElement index, String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.variable = variable;
		this.index = index;
	}

	@Override
	public Object getTarget(Scope scope) {
		Object targetVariable = variable.getTarget(scope);
		Object targetIndex = index.getTarget(scope);
		if (targetVariable == null)
			return null ;
		if (targetIndex == null)
			warning(getFileName(), getLineNumber(), "Can't index with a null value " + index.getLispizedText("") + " in " + variable.getLispizedText(""));
		if (targetVariable instanceof List<?>) {
			if (targetIndex instanceof Integer) {
				return ((List<?>) targetVariable).get(((Integer)targetIndex).intValue());
			} else {
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getLispizedText("") + " in " + variable.getLispizedText(""));
				return null ;
			}
		}
		
		if (targetVariable instanceof String) {
			if (targetIndex instanceof Integer) {
				return ((String) targetVariable).charAt(((Integer)targetIndex).intValue());
			} else {
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getLispizedText("") + " in " + variable.getLispizedText(""));
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
				warning(getFileName(), getLineNumber(), "TargetIndex expected to be an integer " + index.getLispizedText("") + " in " + variable.getLispizedText(""));
				return null ;
			}
		}
		warning(getFileName(), getLineNumber(), "Can't index into " + variable.getLispizedText("") + " Class: " + targetVariable.getClass().getName());
		return null ;
	}

	@Override
	public String getLispizedText(String indent) {
		return getLineNumber() + ":" + indent + "(indexed-access\n"
						+ variable.getLispizedText("  " + indent) + "\n" + index.getLispizedText("  " + indent) + "\n" + indent + ")";
	}
}
