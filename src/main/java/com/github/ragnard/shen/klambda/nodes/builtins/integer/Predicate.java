package com.github.ragnard.shen.klambda.nodes.builtins.integer;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "integer?")
public abstract class Predicate extends BuiltinNode {

    @Specialization
    public Symbol isInteger(long x) {
        return Symbol.TRUE;
    }

    @Specialization
    public Symbol isNotInteger(Object x) {
        return Symbol.FALSE;
    }
}


