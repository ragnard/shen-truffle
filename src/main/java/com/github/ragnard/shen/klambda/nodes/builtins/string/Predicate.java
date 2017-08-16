package com.github.ragnard.shen.klambda.nodes.builtins.string;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "string?")
public abstract class Predicate extends BuiltinNode {

    @Specialization
    public Symbol isString(Object x) {
        return Symbol.fromBoolean(x.getClass() == String.class);
    }
}


