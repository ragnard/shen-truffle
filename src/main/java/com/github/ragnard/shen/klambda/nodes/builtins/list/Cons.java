package com.github.ragnard.shen.klambda.nodes.builtins.list;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "cons")
public abstract class Cons extends BuiltinNode {
    @Specialization
    public com.github.ragnard.shen.klambda.runtime.Cons create(Object x, Object y) {
        return new com.github.ragnard.shen.klambda.runtime.Cons(x, y);
    }
}
