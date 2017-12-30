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

import com.jaliansystems.simpletemplate.TemplateReader;
import com.jaliansystems.simpletemplate.Scope;
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
