package com.github.ragnard.shen.tests;

import com.github.ragnard.shen.tests.categories.Unit;
import com.github.ragnard.shen.klambda.Reader;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@Category(Unit.class)
public class ReaderTest {

    Object read(String s) throws IOException {
        try(PushbackReader sr = new PushbackReader(new StringReader(s))) {
            return Reader.read(sr);
        }
    }

    @Test
    public void readNothing() throws IOException {
        assertEquals(null, read(""));
    }

    @Test
    public void readString() throws IOException {
        assertEquals("", read("\"\""));
        assertEquals("a", read("\"a\""));
        assertEquals("aa", read("\"aa\""));
        assertEquals("aa b ", read("\"aa b \""));
    }

    @Test
    public void readLong() throws IOException {
        assertEquals(0L, read("0"));
        assertEquals(42L, read("42"));
        assertEquals(42L, read("+42"));
        assertEquals(-42L, read("-42"));
        // TODO: sign cancellation, ie. -+--+++-42

    }

    @Test
    public void readDouble() throws IOException {
        assertEquals(0.0, read("0.0"));
        assertEquals(1.42, read("1.42"));
        assertEquals(-1.42, read("-1.42"));
        // TODO: sign cancellation, ie. -+--+++-42.0
    }

    @Test
    public void readSymbol() throws IOException {
        assertEquals(Symbol.intern("a"), read("a"));
        assertEquals(Symbol.intern("ab"), read("ab"));
        assertEquals(Symbol.intern("Ab"), read("Ab"));
    }

    @Test
    public void readList() throws IOException {
        assertEquals(Arrays.asList(), read("()"));
        assertEquals(Arrays.asList(Symbol.intern("a")), read("(a)"));
        assertEquals(Arrays.asList("a"), read("( \"a\")"));
        assertEquals(Arrays.asList(Symbol.intern("bla"), Symbol.intern("honga")), read("(bla  honga )"));
//        assertEquals(Arrays.asList("a", "b", "c"), read("(\"a\")"));
    }

}
