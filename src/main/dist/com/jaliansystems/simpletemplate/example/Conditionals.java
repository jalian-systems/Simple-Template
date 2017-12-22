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
