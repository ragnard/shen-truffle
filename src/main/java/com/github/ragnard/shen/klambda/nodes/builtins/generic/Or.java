package com.github.ragnard.shen.klambda.nodes.builtins.generic;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "or")
public abstract class Or extends BuiltinNode {

    @Specialization
    public Object or(Symbol a, Symbol b) {
        if(a.asBoolean() || b.asBoolean()) {
            return Symbol.TRUE;
        } else {
            return Symbol.FALSE;
        }
    }
}
