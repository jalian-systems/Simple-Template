package com.jaliansystems.simpletemplate;

import java.io.PrintStream;

public class Log {

	private static EvaluationMode mode = EvaluationMode.RELAXED;

	private static PrintStream writer = System.err;

	public static void warning(String message) {
		if (mode == EvaluationMode.STRICT)
			throw new EvaluationError(message);
		else
			writer.println(message);
	}

	public static EvaluationMode getMode() {
		return mode;
	}

	public static void setMode(EvaluationMode mode) {
		Log.mode = mode;
	}

}
