package com.jaliansystems.simpletemplate.example;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Aliases {

	public static void main(String[] args) {
		try {
			withoutAliasAccess();
			aliasAccess();
			aliasAccessUsingSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void withoutAliasAccess() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$addressBook.addresses {\nName: $it.firstName$ $it.lastName$\n$it.phoneNumbers {\n$it.type$ phone: $it.number$\n}$\n}$\n");
		Scope scope = new Scope();
		AddressBook addressBook = AddressBook.populate();
		scope.put("addressBook", addressBook);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void aliasAccess() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$addressBook.addresses {\n$with it {\nName: $firstName$ $lastName$\n$phoneNumbers {\n$with it {\n$type$ phone: $number$\n}$\n}$\n}$\n}$\n");
		Scope scope = new Scope();
		AddressBook addressBook = AddressBook.populate();
		scope.put("addressBook", addressBook);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void aliasAccessUsingSet() throws Exception {
		TemplateReader reader = TemplateReader
				.fromString("$addressBook.addresses {\n$set address to it\n Name: $address.firstName$ $address.lastName$\n$address.phoneNumbers {\n$with it {\n$type$ phone: $number$\n}$\n}$\n}$\n\n");
		Scope scope = new Scope();
		AddressBook addressBook = AddressBook.populate();
		scope.put("addressBook", addressBook);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

}
