package com.github.ragnard.shen.klambda.nodes.builtins.list;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "tl")
public abstract class Tail extends BuiltinNode {
    @Specialization
    public Object tail(Cons cons) {
        if (cons == cons.EMPTY) {
            return cons.EMPTY;
        } else {
            return cons.tl;
        }
    }
}
