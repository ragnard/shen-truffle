package com.github.ragnard.shen.klambda.nodes.builtins.list;

import com.github.ragnard.shen.klambda.nodes.builtins.BuiltinNode;
import com.github.ragnard.shen.klambda.runtime.Cons;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "hd")
public abstract class Head extends BuiltinNode {
    @Specialization
    public Object head(Cons cons) {
        if (cons == Cons.EMPTY) {
            return Cons.EMPTY;
        } else {
            return cons.hd;
        }
    }
}
