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

import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTestSuite {

	static String code_prefix = 
		"import org.junit.runner.RunWith;\n" +
		"import org.junit.runners.Suite;\n" +
		"\n" +
		" @RunWith(Suite.class)\n" +
		" @Suite.SuiteClasses({\n" +
		"\n" ;

	static String code_suffix = 
		"    })\n" +
		"public class AllTests {\n" +
		"}\n" +      
		"\n" ;

	private static String sourceDirectory;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 2) {
			System.err.println("Usage: CreateTestSuite sourceDirectory fileNamePattern");
			System.exit(1);
		}
		ArrayList<String> testClassNames = new ArrayList<String>();
		sourceDirectory = args[1];
		File currentDirectory = new File(sourceDirectory);
		String filePattern = args[0];
		collectFiles(testClassNames, currentDirectory, filePattern);
		PrintStream writer = new PrintStream(new FileOutputStream("AllTests.java"));
		writer.println(code_prefix);
		for (int i = 0; i < testClassNames.size(); i++) {
			String className = testClassNames.get(i);
			writer.print("    " + className
					+ ".class");
			if (i < testClassNames.size() - 1)
				writer.println(",");
			else
				writer.println();
		}
		writer.println(code_suffix);
		writer.close();
		System.out.println(testClassNames);
	}

	private static void collectFiles(ArrayList<String> testFileNames,
			File currentDirectory, final String filePattern) {
		File[] listFiles = currentDirectory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile()
						&& pathname.getName().matches(filePattern);
			}
		});
		List<File> asList = Arrays.asList(listFiles);
		for (File file : asList) {
			testFileNames.add(getRelative(file.getAbsolutePath()));
		}
		File[] listDirs = currentDirectory.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		for (File file : listDirs) {
			collectFiles(testFileNames, file, filePattern);
		}
	}

	private static String getRelative(String absolutePath) {
		if (absolutePath.startsWith(sourceDirectory))
			return absolutePath.substring(sourceDirectory.length() + 1,
					absolutePath.length() - 5).replaceAll("\\/", ".");
		return null;
	}
}
