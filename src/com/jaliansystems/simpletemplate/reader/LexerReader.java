package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class LexerReader {
	private final PushbackReader in;
	private int lineNumber = 1;
	private final String fileName;
	private boolean isPushback;
	private int[] pushbackBuffer = new int[1024];
	private int nPushback = 0 ;
	
	public LexerReader(Reader in) {
		this(in, null);
	}
	
	public LexerReader(Reader in, String fileName) {
		this.in = new PushbackReader(in, 1024);
		this.fileName = fileName == null ? "<stream>" : fileName;
	}
	
	public int read() throws IOException {
		int b = in.read();
		if (b == '\n')
			lineNumber++ ;
		if (isPushback) {
			pushbackBuffer[nPushback++] = b ;
		}
		return b;
	}
	
	public void unread(int c) throws IOException {
		in.unread(c);
		if (c == '\n') {
			lineNumber-- ;
		}
		if (isPushback) {
			nPushback-- ;
		}
	}

	public void unread(char[] ca) throws IOException {
		in.unread(ca);
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void pushback() throws IOException {
		for (int i = nPushback - 1; i >= 0; i--)
			unread(pushbackBuffer[i]);
	}

	public void startPushback() {
		isPushback = true ;
		nPushback = 0 ;
	}
}
