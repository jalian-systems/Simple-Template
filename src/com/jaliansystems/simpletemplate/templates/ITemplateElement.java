package com.jaliansystems.simpletemplate.templates;


public interface ITemplateElement {

	public abstract String apply(Scope scope);

	public abstract boolean asBinary(Scope scope);

}