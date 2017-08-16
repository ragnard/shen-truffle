package com.github.ragnard.shen.klambda.nodes.builtins.generic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "and")
public abstract class And extends BuiltinNode {

    @Specialization
    public Object and(Symbol a, Symbol b) {
        if(a.asBoolean() && b.asBoolean()) {
            return Symbol.TRUE;
        } else {
            return Symbol.FALSE;
        }
    }
}
