/*
 *   Copyright 2010 Jalian Systems Pvt. Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.jaliansystems.simpletemplate.example;

import java.io.IOException;

import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class DefiningMethods {

	public static void main(String[] args) {
		try {
			methodCallABPrint();
			methodCallBoldItalics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void methodCallABPrint() throws Exception, IOException {
		String templateText = "$printAddressDetails (address) {\n" +
		"    <address>\n" +
		"        <firstname>$address.firstName$</firstname>\n" +
		"        <lastname>$address.lastName$</lastname>\n" +
		"        <email>$address.email$</email>\n" +
		"    </address>\n" +
		"}$\n" +
		"$addressBook.addresses {\n" +
		"       $it:printAddressDetails()$\n" +
		"}$\n" ;

		TemplateReader reader = TemplateReader
				.fromString(templateText);
		Scope scope = new Scope();
		AddressBook book = AddressBook.populate();
		scope.put("addressBook", book);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

	private static void methodCallBoldItalics() throws Exception, IOException {
		String templateText =  
			"$italics(s) ${ \"<i>\" $s$ \"</i>\" }$\n" +
			"$bold(s) ${ \"<b>\" $s$ \"</b>\" }$\n" +
			"\n" +
			"${ $set hello to \"Hello\" $hello:bold():italics()$ }$\n" +
			"\n" +
			"\n" ;

		TemplateReader reader = TemplateReader
				.fromString(templateText);
		Scope scope = new Scope();
		AddressBook book = AddressBook.populate();
		scope.put("addressBook", book);
		TemplateElement template = reader.readTemplate();
		String result = template.apply(scope);
		System.out.println("Result " + result);
	}

}
