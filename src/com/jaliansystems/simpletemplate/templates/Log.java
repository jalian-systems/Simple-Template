package com.jaliansystems.simpletemplate.templates;

import java.io.PrintStream;

public class Log {

	private static PrintStream writer = System.err ;
	
	public static void setWriter(PrintStream writer) {
		Log.writer = writer;
	}

	public static void reset() {
		Log.writer = System.err ;
	}
	
	public static void warning(String message) {
		writer.println(message);
	}
}
