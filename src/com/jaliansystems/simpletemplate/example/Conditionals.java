package com.jaliansystems.simpletemplate.example;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class Conditionals {

	public static void main(String[] args) {
		try {
			ifelseConditionalAccess();
			ifelseConditionalAccessFalse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void ifelseConditionalAccess() throws Exception {
		String text =
	        "This address book belongs to: $addressBook.belongs$\n" +"$ifelse addressBook.addresses\n" +
			"    $addressBook.addresses {\n" +
			"       $set address to it\n" +
			"       Name: $address.firstName$ $address.lastName$\n" +
			"       $address.phoneNumbers {\n" +
			"           $with it {\n" +
			"               $type$ phone: $number$\n" +
			"           }$\n" +
			"       }$\n" +
			"    }$\n" +
			"    \"This address book does not have any entries\"\n";
		TemplateReader reader = TemplateReader
				.fromString(text);
		Scope scope = new Scope();
		AddressBook addressBook = AddressBook.populate();
		scope.put("addressBook", addressBook);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);		
	}

	private static void ifelseConditionalAccessFalse() throws Exception {
		String text =
	        "This address book belongs to: $addressBook.belongs$\n" +"$ifelse addressBook.addresses {\n" +
			"    $addressBook.addresses {\n" +
			"       $set address to it\n" +
			"       Name: $address.firstName$ $address.lastName$\n" +
			"       $address.phoneNumbers {\n" +
			"           $with it {\n" +
			"               $type$ phone: $number$\n" +
			"           }$\n" +
			"       }$\n" +
			"    }$\n" +
			"}$\n" +
			"{" +
			"    This address book does not have any entries\n" +
			"}$\n" ;
		TemplateReader reader = TemplateReader
				.fromString(text);
		Scope scope = new Scope();
		AddressBook addressBook = new AddressBook("John Doe");
		scope.put("addressBook", addressBook);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result\n" + result);
	}

}
