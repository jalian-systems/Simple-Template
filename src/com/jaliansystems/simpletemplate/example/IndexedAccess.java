package com.jaliansystems.simpletemplate.example;

import java.util.HashMap;
import java.util.Map;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class IndexedAccess {

	public static void main(String[] args) {
		try {
			listAccess();
			mapAccess();
			mapAccessUsingModelVariable();
			mapAccessUsingEvaluatedVariable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void listAccess() throws Exception {
		TemplateReader reader = TemplateReader.fromString("$flowers[0]$");
		Scope scope = new Scope();
		String[] flowers = new String[] { "Rose", "Jasmine", "Lily" } ;
		scope.put("flowers", flowers);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}


	private static void mapAccessUsingModelVariable() throws Exception {
		TemplateReader reader = TemplateReader.fromString("$capitals[mycountry]$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);
		scope.put("mycountry", "India");

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void mapAccessUsingEvaluatedVariable() throws Exception {
		TemplateReader reader = TemplateReader.fromString("$capitals[$mycountry$]$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);
		scope.put("mycountry", "India");

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void mapAccess() throws Exception {
		TemplateReader reader = TemplateReader.fromString("$capitals[\"United States\"]$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}


}
