package com.jaliansystems.simpletemplate.example;

import java.io.IOException;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class AttributeAccess {

	public static class Address {
		private String name;
		private String email;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static void main(String[] args) {
		try {
			simpleAccess();
			accessThroughComposite();
			listAccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void listAccess() throws Exception {
		TemplateReader reader = TemplateReader.fromString("$set st_list_separator to \"--\"$set st_list_prefix to \"<flower:\"$set st_list_suffix to \"/>\"$flowers$");
		Scope scope = new Scope();
		String[] flowers = new String[] { "Rose", "Jasmine", "Lily" } ;
		scope.put("flowers", flowers);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void accessThroughComposite() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("Name is $address.name$ and email is $address.email$");
		Scope scope = new Scope();
		Address address = new Address();
		address.setName("John Doe");
		address.setEmail("john.doe@example.com");
		scope.put("address", address);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void simpleAccess() throws Exception, IOException {
		TemplateReader reader = TemplateReader
				.fromString("Name is $name$ and email is $email$");
		Scope scope = new Scope();
		scope.put("name", "John Doe");
		scope.put("email", "john.doe@example.com");
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

}
