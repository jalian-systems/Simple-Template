package com.jaliansystems.simpletemplate.example;

import java.util.HashMap;
import java.util.Map;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Looping {

	public static void main(String[] args) {
		try {
			mapLoop();
			listLoop();
			mapKeyLoop();
			mapValuesLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mapLoop() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$capitals {    Capital of $key$ is $value$\n}$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);
	}

	private static void listLoop() throws Exception {
		TemplateReader reader = TemplateReader.fromString("List of flowers available:\n$flowers{    $index1$: $it$\n}$");
		Scope scope = new Scope();
		String[] flowers = new String[] { "Rose", "Jasmine", "Lily" } ;
		scope.put("flowers", flowers);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);
	}


	private static void mapKeyLoop() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$capitals.keySet {    $it$\n}$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);
	}

	private static void mapValuesLoop() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$capitals.values {    $it$\n}$");
		Scope scope = new Scope();
		Map<String, String> capitals = new HashMap<String, String>();
		capitals.put("India", "New Delhi");
		capitals.put("United States", "Washington");
		capitals.put("Canada", "Ottawa");
		scope.put("capitals", capitals);

		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);
	}

}
