package com.github.ragnard.shen.util;

import java.io.IOException;
import java.io.Reader;


public class IndexedPushbackReader extends java.io.PushbackReader {

    private int position;

    public IndexedPushbackReader(Reader r) {
        super(r);
        this.position = 0;
    }

    public int getPosition() {
        return this.position;
    }

    public int read() throws IOException {
        int ch = super.read();
        this.position++;
        return ch;
    }

    public void unread(int ch) throws IOException {
        super.unread(ch);
        this.position--;
    }
}
