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
