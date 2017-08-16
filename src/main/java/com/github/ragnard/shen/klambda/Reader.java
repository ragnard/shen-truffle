package com.github.ragnard.shen.klambda;

import com.github.ragnard.shen.klambda.runtime.Symbol;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {

    public interface IEOFHandler {
        Object eof() throws IOException;
    }

    public static final IEOFHandler ReturnNullOnEof = () -> null;

    public static final IEOFHandler ThrowOnEof = () -> { throw new EOFException(); };


    public static List<Object> readAll(PushbackReader r) throws IOException {
        ArrayList<Object> l = new ArrayList<Object>();

        Object res;
        while((res = read(r)) != null) {
            l.add(res);
        }

        return l;
    }

    public static Object read(PushbackReader r) throws IOException {
        return read(r, ReturnNullOnEof);
    }

    public static Object read(PushbackReader r, IEOFHandler eofHandler) throws IOException {
        int ch;

        do {
            ch = r.read();
        } while (Character.isWhitespace(ch));

        switch(ch) {
            case -1:  return eofHandler.eof();
            case ')': throw new RuntimeException("unmatched ')'");
            case '(': return readList(r);
            case '"': return readString(r);
            default:  return readToken(r, eofHandler, ch);
        }
    }

    private static List readList(PushbackReader r) throws IOException {
        ArrayList list = new ArrayList();

        while(true) {
            int ch = r.read();
            if (ch == -1) throw new EOFException();
            if (ch == ')') break;
            r.unread(ch);
            list.add(read(r, ThrowOnEof));
        }
        return list;
    }

    private static String readString(PushbackReader r) throws IOException {
        StringBuilder sb = new StringBuilder();
        while(true) {
            int ch = r.read();
            if (ch == -1) throw new EOFException();
            if (ch == '"') break;
            sb.append((char)ch);
        }
        return sb.toString();
    }

    private static Object readToken(PushbackReader r, IEOFHandler ieofHandler, int ch) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append((char)ch);
        while(true) {
            ch = r.read();
            if (ch == -1) {
                ieofHandler.eof();
                break;
            } else if (Character.isWhitespace(ch)) {
                break;
            } else if (ch == ')') {
                r.unread(ch);
                break;
            } else {
                sb.append((char)ch);
            }
        }
        return interpretToken(sb.toString());
    }

    private static Pattern NUMBER_PATTERN = Pattern.compile("([-+]?[0-9]+(?<fraction>\\.[0-9]*)?(?<exponent>[eE][-+]?[0-9]+)?)");

    private static Object interpretToken(String token) {
        Matcher matcher = NUMBER_PATTERN.matcher(token);
        if(matcher.matches()) {
            if (matcher.group("fraction") != null || matcher.group("exponent") != null) {
                return Double.parseDouble(token);
            } else {
                return Long.parseLong(token);
            }
        } else {
            return Symbol.intern(token);
        }
    }

}
