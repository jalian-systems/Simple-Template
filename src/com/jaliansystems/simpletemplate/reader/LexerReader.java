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
	private final String tokenStart;
	private String tokenEnd;
	
	public LexerReader(Reader in, String fileName) {
		this(in, fileName, "$");
	}
	
	public LexerReader(Reader in, String fileName, String tokenStart) {
		this(in, fileName, tokenStart, tokenStart);
	}

	public LexerReader(Reader in, String fileName, String tokenStart, String tokenEnd) {
		this.in = new PushbackReader(in, 1024);
		this.fileName = fileName == null ? "<stream>" : fileName;
		this.tokenStart = tokenStart;
		this.tokenEnd = tokenEnd ;
	}

	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getTokenStart() {
		return tokenStart;
	}
	
	public String getTokenEnd() {
		return tokenEnd;
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
		for (int i = ca.length - 1; i >= 0; i--)
			unread((int)ca[i]);
	}
	
	public void pushback() throws IOException {
		for (int i = nPushback - 1; i >= 0; i--)
			unread(pushbackBuffer[i]);
	}

	public void startPushback() {
		isPushback = true ;
		nPushback = 0 ;
	}

	public int read(char[] la) throws IOException {
		int i = 0 ;
		for (; i < la.length; i++) {
			int c = in.read();
			if (c == -1)
				break ;
			la[i] = (char) c ;
		}
		return i;
	}
}
