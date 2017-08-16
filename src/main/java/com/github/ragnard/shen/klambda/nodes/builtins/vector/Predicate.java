package com.github.ragnard.shen.klambda.nodes.builtins.vector;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "absvector?")
public abstract class Predicate extends BuiltinNode {
    @Specialization
    public static Symbol isVector(Object x) {
        return Symbol.fromBoolean(x.getClass() == Object[].class);
    }
}
