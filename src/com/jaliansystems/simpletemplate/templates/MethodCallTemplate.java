package com.jaliansystems.simpletemplate.templates;

import java.util.List;

import com.jaliansystems.simpletemplate.Log;

public class MethodCallTemplate extends TemplateElement {

	private final String name;
	private final List<TemplateElement> paramValues;
	private MethodCallTemplate next;

	public MethodCallTemplate(String name, List<TemplateElement> paramValues,
			String fileName, int lineNumber) {
		super(fileName, lineNumber);
		this.name = name;
		this.paramValues = paramValues;
	}

	@Override
	public Object getTarget(Scope scope) {
		Object o = scope.get(name);
		if (o == null) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name);
			return "";
		}
		if (!(o instanceof MethodDefinitionTemplate)) {
			Log.warning(getFileName(), getLineNumber(),
					"Unspecified subtemplate - " + name + " Found a " + o.getClass().getName() + " for " + name);
			return "";
		}
		MethodDefinitionTemplate st = (MethodDefinitionTemplate) o;
		List<String> paramNames = st.getParams();
		if (paramNames.size() != paramValues.size()) {
			Log.warning(getFileName(), getLineNumber(), "For subtemplate "
					+ name + " expecting " + paramNames.size()
					+ " parameters. Got: " + paramValues.size() + " parameters");
			return "";
		}
		Scope methodCallScope = new Scope();
		for (int i = 0; i < paramNames.size(); i++)
			methodCallScope.put(paramNames.get(i), paramValues.get(i).getTarget(scope));
		Object result = st.getTemplate().apply(methodCallScope);
		return applyChain(result, scope);
	}

	private Object applyChain(Object result, Scope scope) {
		MethodCallTemplate template = next ;
		while (template != null) {
			template.setVariable(new LiteralTextTemplate(result.toString(), getFileName(), getLineNumber()));
			result = template.getTarget(scope);
			template = template.getNext();
		}
		return result;
	}

	private MethodCallTemplate getNext() {
		return next;
	}

	@Override
	public String getLispizedText(String indent) {
		StringBuffer sb = new StringBuffer();
		sb.append(getLineNumber() + ":" + indent + "(call " + name + "\n") ;
		sb.append(indent + "  " + "(paramlist\n");
		for (TemplateElement t : paramValues) {
			sb.append(t.getLispizedText("    " + indent)).append("\n");
		}
		sb.append(indent + "  " + ")\n");
		if (next != null) {
			sb.append(indent + "  " + "(next-in-chain\n");
			sb.append(next.getLispizedText("    " + indent));
		}
		sb.append(indent + ")");
		return sb.toString();
	}

	public void setVariable(TemplateElement vt) {
		paramValues.add(0, vt);
	}

	public void setNext(MethodCallTemplate next) {
		this.next = next;
	}

}
