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

package com.jaliansystems.simpletemplate.example.ABExport;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.reader.LexerException;
import com.jaliansystems.simpletemplate.reader.ParserException;
import com.jaliansystems.simpletemplate.TemplateReader;
import com.jaliansystems.simpletemplate.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class ABExporter {
	private AddressBook addressBook;

	public ABExporter() {
		addressBook = AddressBook.populate();
	}

	private void export(String templateFile, String outputFile)
			throws Exception {
		TemplateReader reader = new TemplateReader(new FileReader(templateFile), templateFile, "$", "$");
		reader.setEvaluationMode(EvaluationMode.STRICT);
		Scope scope = new Scope();
		scope.put("addressBook", addressBook);
		TemplateElement template = null;
		try {
			template = reader.readTemplate();
		} catch (LexerException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ParserException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String result = template.apply(scope);

		Writer writer = new FileWriter(outputFile);
		writer.write(result);
		writer.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ABExporter exporter = new ABExporter();
		try {
			exporter.export("addressbook.st", "addressbook.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
