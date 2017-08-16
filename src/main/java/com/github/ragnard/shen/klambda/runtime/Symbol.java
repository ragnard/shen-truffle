package com.github.ragnard.shen.klambda.runtime;

import java.util.concurrent.ConcurrentHashMap;

public class Symbol {

    private static final ConcurrentHashMap<String, Symbol> symbols = new ConcurrentHashMap<>();

    public static Symbol intern(String name) {
        return symbols.computeIfAbsent(name, s -> new Symbol(s));
    }

    public static final Symbol TRUE = intern("true");
    public static final Symbol FALSE = intern("false");

    private final String name;

    protected Symbol(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name was null");
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Symbol fromBoolean(boolean b) {
        return b ? TRUE : FALSE;
    }

    public boolean asBoolean() {
        if (this == TRUE) {
            return true;
        } else if (this == FALSE) {
            return false;
        } else {
            throw new RuntimeException("asBoolean: " + this);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        return name.equals(symbol.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
