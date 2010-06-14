package com.jaliansystems.simpletemplate.example;

import java.io.IOException;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class BlockStatements {

	public static void main(String[] args) {
		try {
			blocksText();
			blocksTemplate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void blocksText() throws Exception, IOException {
		TemplateReader reader = TemplateReader
				.fromString("${ {\"Hello World\"}$ }$");
		Scope scope = new Scope();
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void blocksTemplate() throws Exception, IOException {
		TemplateReader reader = TemplateReader
				.fromString("${ \"Hello World\" }$");
		Scope scope = new Scope();
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

}
