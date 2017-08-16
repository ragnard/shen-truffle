package com.github.ragnard.shen.klambda.nodes.builtins.list;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.github.ragnard.shen.klambda.runtime.Symbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "cons?")
public abstract class Predicate extends BuiltinNode {
    @Specialization
    public Symbol isList(Object x) {
        return Symbol.fromBoolean(x.getClass() == Cons.class && x != Cons.EMPTY);
    }
}
