package com.jaliansystems.simpletemplate.templates;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import com.jaliansystems.simpletemplate.reader.SimpleTemplateException;

public abstract class TemplateElement {
	public abstract Object getTarget(Scope scope);

	private String fileName ;
	private int lineNumber ;
	
	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.TemplateElement#apply(java.util.Map)
	 */
	public String apply(Scope scope) {
		Object object = getTarget(scope);
		if (object == null) {
			return "";
		}
		return object.toString();
	}

	/* (non-Javadoc)
	 * @see com.jaliansystems.simpletemplate.TemplateElement#asBinary(java.util.Map)
	 */
	public final boolean asBinary(Scope scope) {
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
	
	@Override
	public final String toString() {
		return getName();
	}
	
	public abstract String getName();

	public void evaluate(Scope scope, Writer writer) throws SimpleTemplateException, IOException {
		
		String result = apply(scope);
		
		writer.write(result);
		writer.flush();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
}
