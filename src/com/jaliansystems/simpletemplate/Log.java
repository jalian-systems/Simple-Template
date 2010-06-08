package com.jaliansystems.simpletemplate;

import java.io.PrintStream;

public class Log {

	private static EvaluationMode mode = EvaluationMode.RELAXED;

	private static PrintStream writer = System.err;

	public static void warning(String fileName, int lineNumber, String message) {
		if (mode == EvaluationMode.STRICT)
			throw new EvaluationError(fileName + ":" + lineNumber + ": error " + message);
		else
			writer.println(fileName + ":" + lineNumber + ": warning " + message);
	}

	public static void setMode(EvaluationMode mode) {
		Log.mode = mode;
	}

}
