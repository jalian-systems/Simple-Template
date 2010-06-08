package com.jaliansystems.simpletemplate.reader;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.net.URL;

public class LexerReader {
	private final PushbackReader in;
	private int lineNumber = 1;
	private final String fileName;
	private boolean isMarked;
	private int[] markBuffer = new int[1024];
	private int nMarked = 0;
	private final String tokenStart;
	private String tokenEnd;
	private final URL contextURL;

	public LexerReader(URL url, Reader in, String fileName, String tokenStart,
			String tokenEnd) {
		this.contextURL = url;
		this.in = new PushbackReader(in, 1024);
		this.fileName = fileName == null ? "<stream>" : fileName;
		if (tokenStart == null) {
			tokenStart = "$";
		}
		if (tokenEnd == null)
			tokenEnd = tokenStart;
		this.tokenStart = tokenStart;
		this.tokenEnd = tokenEnd;
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
			lineNumber++;
		if (isMarked) {
			markBuffer[nMarked++] = b;
		}
		return b;
	}

	public void unread(int c) throws IOException {
		in.unread(c);
		if (c == '\n') {
			lineNumber--;
		}
		if (isMarked) {
			nMarked--;
		}
	}

	public void unread(char[] ca) throws IOException {
		for (int i = ca.length - 1; i >= 0; i--)
			unread((int) ca[i]);
	}

	public void reset() throws IOException {
		for (int i = nMarked - 1; i >= 0; i--)
			unread(markBuffer[i]);
	}

	public void mark() {
		isMarked = true;
		nMarked = 0;
	}

	public int read(char[] la) throws IOException {
		int i = 0;
		for (; i < la.length; i++) {
			int c = in.read();
			if (c == -1)
				break;
			la[i] = (char) c;
		}
		return i;
	}

	public URL getContextURL() {
		return contextURL;
	}
}
