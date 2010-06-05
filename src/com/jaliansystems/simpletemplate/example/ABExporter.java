package com.jaliansystems.simpletemplate.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jaliansystems.simpletemplate.reader.LexerException;
import com.jaliansystems.simpletemplate.reader.ParserException;
import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.TemplateElement;
import com.jaliansystems.simpletemplate.templates.Scope;

public class ABExporter {
	private AddressBook addressBook ;
	
	public ABExporter() {
		populate();
	}
	
	private void populate() {
		addressBook = new AddressBook("Dakshinamurthy Karra");
		AddressBookEntry abe = new AddressBookEntry("Dakshinamurthy", "Karra");
		Address address = new Address("M.M.Layout", "Kavalbyrasandra", "560 032", "India");
		abe.setAddress(address);
		abe.setEmail("dakshinamurthy.karra@jaliansystems.com");
		abe.setAffiliation("Linux India", "Founding Member");
		abe.setAffiliation("ASCII", "Founding Board Member");
		abe.setPhoneNumber("work", "+919845058872");
		abe.setPhoneNumber("home", "+918023432215");
		addressBook.add(abe);
	}

	private void export(String templateFile, String outputFile) throws IOException {
		TemplateReader reader = new TemplateReader(new FileReader(templateFile), templateFile);
		Scope scope = new Scope();
		scope.put("addressbook", addressBook);
		TemplateElement template = null;
		try {
			template = reader.readTemplate();
		} catch (LexerException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (ParserException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		String result = template.apply(scope);
		System.out.println(template.toString());
		
		FileWriter writer = new FileWriter(outputFile);
		writer.write(result);
		writer.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ABExporter exporter = new ABExporter();
		try {
			exporter.export("export.st", "export.out");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
