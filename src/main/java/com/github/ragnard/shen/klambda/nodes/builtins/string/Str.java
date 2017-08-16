package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Function;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "str")
public abstract class Str extends BuiltinNode {
    
    @Specialization
    @CompilerDirectives.TruffleBoundary
    public String str(long n) {
        return Long.toString(n);
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    public String str(double n) {
        return Double.toString(n);
    }

    @Specialization
    public String str(String s) {
        return s;
    }

    @Specialization
    public String str(Symbol symbol) {
        return symbol.getName();
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    public String str(Function f) {
        return f.toString();
    }
}


