package com.jaliansystems.simpletemplate.example;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class HelloSimpleTemplate {

	public static void main(String[] args) {
		try {
			TemplateReader reader = TemplateReader.fromString("Hello $greeting$");
			TemplateElement template = reader.readTemplate();
			Scope scope = new Scope();
			scope.put("greeting", "Simple Template");
			String result = template.apply(scope);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
