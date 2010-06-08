package com.jaliansystems.simpletemplate.example;

import java.io.IOException;
import java.io.Writer;

public class SpaceRemovingWriter extends Writer {

	private final Writer writer;
	private final char[] cline = new char[2048] ;
	private int nchar = 0;
	
	public SpaceRemovingWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < len; i++) {
			cline[nchar++] = cbuf[i];
			if (cbuf[i] == '\n')
				processLine();
		}
	}

	private void processLine() throws IOException {
		for (int i = 0; i < nchar; i++) {
			if (!Character.isWhitespace(cline[i])) {
				writer.write(cline, 0, nchar);
				nchar = 0 ;
				return ;
			}
		}
		nchar = 0 ;
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		if (nchar != 0)
			writer.write(cline, 0, nchar);
		writer.close();
	}

}
