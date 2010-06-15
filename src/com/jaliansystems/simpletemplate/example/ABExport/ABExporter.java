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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.jaliansystems.simpletemplate.EvaluationMode;
import com.jaliansystems.simpletemplate.reader.LexerException;
import com.jaliansystems.simpletemplate.reader.ParserException;
import com.jaliansystems.simpletemplate.reader.TemplateReader;
import com.jaliansystems.simpletemplate.templates.Scope;
import com.jaliansystems.simpletemplate.templates.TemplateElement;

public class ABExporter {
	private AddressBook addressBook;

	public ABExporter() {
		addressBook = AddressBook.populate();
	}

	private void export(String templateFile, String outputFile)
			throws Exception {
		TemplateReader reader = new TemplateReader(
				new FileReader(templateFile), templateFile, "$", "$");
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

		result = removeSpaces(result);
		Document document = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(result.getBytes()));
		writeXmlFile(document, outputFile);
//		Writer writer = new FileWriter(outputFile);
//		writer.write(result);
//		writer.close();
	}

	private String removeSpaces(String result) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(result));
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			if (!line.isEmpty())
				sb.append(line);
			line = br.readLine();
		}
		return sb.toString();
	}

	public static void writeXmlFile(Document doc, String filename) {
		try {
			// Prepare the DOM document for writing
			Source source = new DOMSource(doc);

			// Prepare the output file
			File file = new File(filename);
			Result result = new StreamResult(file);

			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
	        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		}
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
