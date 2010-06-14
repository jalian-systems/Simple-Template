package com.jaliansystems.simpletemplate.example;

import java.io.IOException;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class UsingInclude {

	public static void main(String[] args) {
		try {
			printUsingInclude();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printUsingInclude() throws Exception, IOException {
		TemplateReader reader = TemplateReader
				.fromFile("template.st");
		Scope scope = new Scope();
		AddressBook book = AddressBook.populate();
		scope.put("addressBook", book);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

}
